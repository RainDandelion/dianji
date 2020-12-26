package com.NIO.Server;

import com.NIO.Server.entity.Device;
import com.NIO.Server.utils.DeviceProfile;
import com.NIO.Server.utils.NioExecutorServerPool;
import com.NIO.Server.utils.StateEnum;
import com.NIO.Server.utils.StaticElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class WebCameraServer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());



    private NioExecutorServerPool serverPool = new NioExecutorServerPool(5,10);

    /**
     * 服务器接收处理线程
     */
    private ServerSocketChannel server;
    private Selector selector;
    private Thread serverThread;

    @Value("${devices.port}")
    private String port;

    @Value("${devices.left}")
    private String left;  //5
    @Value("${devices.right}")
    private String right; //4
    @Value("${devices.dir}")
    private String dirstr;  //1

    private Timer timer=new Timer();  //定时任务线程


    private int leftIndex=4;
    private int rightIndex=5;
    private byte dirleft =0;
    private byte dirright =1;

    @Resource
    private DeviceProfile settingsDevices;

    private Map<Integer, Device> deviceList = new HashMap<>();

    @PostConstruct
    public void initMessage(){
        logger.info("webCameraInit ");
        this.init();
    }

    public void closeMessage(){
        logger.info("webCameraClose");
        this.close();
    }

    public WebCameraServer(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (selector !=null){
                    for (SelectionKey key:
                         selector.selectedKeys()) {
                        if (key.channel() instanceof SocketChannel){
                            ByteBuffer bb = ByteBuffer.wrap(new byte[]{0});

                            try {
                                ((SocketChannel)key.channel()).write(bb);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        };
        timer.schedule(timerTask,5000,5000);
    }


    private void init() {

        /**
         * 参数的初始化
         */
        try{
            leftIndex=Integer.parseInt(left);
        }catch (Exception e)
        {
            logger.info("left format error");
        }

        try{
            rightIndex=Integer.parseInt(right);
        }catch (Exception e)
        {
            logger.info("right format error");
        }
        try{

            dirleft =Byte.parseByte(dirstr); //dirstr = 1
            dirright = (byte) (dirleft == (byte) 0 ? 1 : 0);
        }catch (Exception e)
        {
            logger.info("dir format error");
        }

        for (int i = 0; i < settingsDevices.getDevicelist().size(); i++) {
            deviceList.put(i,new Device());
            deviceList.get(i).setCameraIP(settingsDevices.getDevicelist().get(i).get(StaticElement.CAMERAIP));
            deviceList.get(i).setMotoIp(settingsDevices.getDevicelist().get(i).get(StaticElement.MOTOIP));
        }

        /**
         * 连接相关
         */
        try {

            if (selector ==null){
                selector = Selector.open();
            }
            if (serverThread ==null || (!serverThread.isAlive())){
                serverThread = new Thread(new Runnable() {
                    private boolean runFlag = true;
                    private SocketChannel socketChannel;
                    @Override
                    public void run() {

                        while(runFlag){
                            if (server ==null ||(!server.isOpen())){
                                try {
                                    server = ServerSocketChannel.open();
                                    server.bind(new InetSocketAddress(Integer.parseInt(port)));
                                    server.configureBlocking(false);
                                    server.register(selector,SelectionKey.OP_ACCEPT);
                                    logger.info("server port" + port);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    runFlag = false;
                                }
                            }

                            while (runFlag){
                                try {
                                    if (selector.select(30000) == 0){
                                        continue;
                                    }
                                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                                    while (iterator.hasNext()){
                                        SelectionKey selectionKey = iterator.next();
                                        if (selectionKey.isAcceptable()){  //开始握手
                                            socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
                                            String ip = socketChannel.socket().getInetAddress().getHostAddress();
                                            logger.info("get socket "+ip);
                                            boolean deviceFlag = false;
                                            //遍历列表
                                            for(Device d:deviceList.values())
                                            {
                                                if(d.getMotoIp().equals(ip))
                                                {
                                                    d.setByteBuffer(ByteBuffer.allocate(500));
                                                    d.setSocketChannel(socketChannel);
                                                    socketChannel.configureBlocking(false);
                                                    socketChannel.register(selector, SelectionKey.OP_READ, d);
                                                    logger.info("selector register ip");
                                                    resetPosition(d);
                                                    deviceFlag=true;
                                                    break;
                                                }
                                            }

                                        }
                                        if (selectionKey.isReadable()){
                                            handleRead(selectionKey);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }



                        }
                    }
                }
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public  Map<String,Object> moto(int id, byte dir){
        Map<String,Object> result=new HashMap<>();
        Device device = deviceList.get(id);
        if (device == null){
            logger.info("moto no device, maybe ip error");
            result.put(StaticElement.ERROR,StateEnum.NIOD.getMessage());
            result.put(StaticElement.RESULT,StateEnum.NIOD.getCode());
            return result;
        }
        if(device.getSocketChannel()==null||(!device.getSocketChannel().isConnected())) {//没有这个通道，或处于关闭状态
            result.put(StaticElement.ERROR,StateEnum.OFFLINE.getMessage());
            result.put(StaticElement.RESULT,StateEnum.OFFLINE.getCode());
            return  result;
        }
        if (dir == StaticElement.leftDir){
            if(((device.getErrorFlag()>>(leftIndex-1))&1)!=0) {//位运算 error flag
                //到底了
                result.put(StaticElement.ERROR,StateEnum.BORDERLINE.getMessage());
                result.put(StaticElement.RESULT,StateEnum.BORDERLINE.getCode());
                return  result;
            }
        }else if(dir==StaticElement.rightDir) {
            if(((device.getErrorFlag()>>(rightIndex-1))&1)!=0) {//到底了
                result.put(StaticElement.ERROR,StateEnum.BORDERLINE.getMessage());
                result.put(StaticElement.RESULT,StateEnum.BORDERLINE.getCode());
                return  result;
            }
        }else{
            result.put(StaticElement.ERROR,StateEnum.ERRORCMD.getMessage());
            result.put(StaticElement.RESULT,StateEnum.ERRORCMD.getCode());
            return  result;
        }
        ByteBuffer bb=ByteBuffer.wrap(new byte[]{(byte) 0xf5,0x05,0x02,0,0,2, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,0x10});
        bb.put(3, dir == StaticElement.leftDir ? this.dirleft : this.dirright);
        bb.rewind();
        try {
            device.getSocketChannel().write(bb);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("send moto data error");
        }
        result.put(StaticElement.RESULT,StateEnum.OK.getCode());
        return  result;
    }


    private void handleRead(SelectionKey selectionKey) {
        SocketChannel socketChannel=(SocketChannel)selectionKey.channel();
        Device device = (Device) selectionKey.attachment();
        if(device==null) {
            logger.error("socket read no device bind");
        }
        if (!(socketChannel.isOpen())){

            try {
                selectionKey.cancel();
                socketChannel.close();
                device.setSocketChannel(null);
                logger.error("socket read socket had been closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        ByteBuffer byteBuffer = device.getByteBuffer();

        try {
            int length = socketChannel.read(byteBuffer);
            if (length<=0){
                logger.info("socket read 0 socket had been closed");
                selectionKey.cancel();
                socketChannel.close();
                device.setSocketChannel(null);
                return;
            }
            byteBuffer.flip();

            byteBuffer.compact();


        } catch (IOException e) {
            e.printStackTrace();
            logger.info("socket read error");
            device.setSocketChannel(null);
            selectionKey.cancel();

            try {
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }

    private void resetPosition(Device d) {
        ByteBuffer bb=ByteBuffer.wrap(new byte[]{(byte) 0xf5,0x05,03,00, (byte) 0xff,0,0,0,0,0,0});
        while (bb.remaining() >0){
            try {
                d.getSocketChannel().write(bb);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void close() {
        //关闭定时器
        timer.cancel();
        //停止事件处理线程
        serverThread.interrupt();
        //关闭线程池

        //关闭所有channel
        for(SelectionKey key:selector.keys())
        {
            try {
                key.channel().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //关闭selector
        try {
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *  获取编号为id 的设备
     * @param id
     * @return
     */
    public Device get(int id) {
        return deviceList.get(id);
    }


}
