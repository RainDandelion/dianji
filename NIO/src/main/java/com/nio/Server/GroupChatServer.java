package com.nio.Server;


import com.nio.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

@Service
public class GroupChatServer implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(GroupChatServer.class);
    //线程池
    //ServerHandlerExcutePool executors= new ServerHandlerExcutePool(50,100);
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    @Value("${nio.port}")
    public Integer PORT;


    //构造器
    //初始化工作
    public GroupChatServer() {
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        try {

            //得到选择器
            selector = Selector.open();
            //ServerSocketChannel
            listenChannel =  ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));

            logger.info("监听端口：{}",PORT);

            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将该listenChannel 注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen() {

        logger.info("监听线程: {}",Thread.currentThread().getName());

        try {

            //循环处理
            while (true) {

                int count = selector.select();
//                if (count == 0){
//                    logger.info("目前无连接");
//                    continue;
//                }
                if(count > 0) {//有事件处理

                    //遍历得到selectionKey 集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        //取出selectionkey
                        SelectionKey key = iterator.next();
                        //当前的key 删除，防止重复处理
                        iterator.remove();
                        //监听到accept
                        if(key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();

                            sc.configureBlocking(false);
                            //将该 sc 注册到seletor
                            sc.register(selector, SelectionKey.OP_READ);

                            //提示
                            //System.out.println(sc.getRemoteAddress() + " 上线 ");
                            logger.info("客户端：{} 上线",sc.getRemoteAddress());
                        }
                        if(key.isReadable()) { //通道发送read事件，即通道是可读的状态
                            //处理读 (专门写方法..)
//                            executors.execute(()->{
                                readData(key);
//                            });
                        }

                    }

                }
            }

        }catch (Exception e) {
            e.printStackTrace();

        }finally {
            //发生异常处理....
//            try {
//                selector.close();
//                listenChannel.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    //读取客户端消息
    private void readData(SelectionKey key) {


        //取到关联的channle
        SocketChannel channel = null;

        try {
            //得到channel
             channel = (SocketChannel) key.channel();
            //创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int count = channel.read(buffer);
            //根据count的值做处理
            if(count > 0) {
                buffer.flip();
                //把缓存区的数据转成字符串
               // String msg = new String(buffer.array());
                byte[] bytes  = new byte[buffer.remaining()];

                buffer.get(bytes);
                //输出该消息
                logger.info("来自客户端 ，{} ，的消息：{}",((SocketChannel) key.channel()).getRemoteAddress().toString(), ByteUtil.BinaryToHexString(bytes));
                //向客户端转发消息, 专门写一个方法来处理
                sendInfo(bytes);
            }
        }catch (IOException e) {
            try {
                //System.out.println(channel.getRemoteAddress() + " 离线了..");
                logger.info("{},离线了",channel.getRemoteAddress());
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            }catch (IOException e2) {
                e2.printStackTrace();;
            }
        }
    }

    //转发消息给其它客户(通道)
    private void sendInfo(byte[] msg ) throws  IOException{


        //遍历 所有注册到selector 上的 SocketChannel,并排除 self
        for(SelectionKey key: selector.keys()) {
            //通过 key  取出对应的 SocketChannel
            Channel targetChannel = key.channel();

            //排除自己
            //&& targetChannel != self
            if(targetChannel instanceof  SocketChannel ) {

                //转型
                SocketChannel dest = (SocketChannel)targetChannel;


                //String temp = "f5 05 02 01 00 02 ff ff ff ff ff ff 12";
                //ByteBuffer writeBuffer = ByteBuffer.allocate(msg.length);
                ByteBuffer writeBuffer=ByteBuffer.wrap(new byte[]{(byte) 0xf5,0x05,0x02,1,0,2, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,0x10});

                writeBuffer.rewind();
                dest.write(writeBuffer);

            }
        }

    }
    //关闭
    public void close(){
        if (listenChannel !=null && (listenChannel.isOpen())){
            try {
                listenChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//
//    /**
//     * 字节数组转为16进制
//     * @param bytes
//     * @return
//     */
//    public static String bytesToHex(byte[] bytes) {
//        StringBuffer sb = new StringBuffer();
//        for(int i = 0; i < bytes.length; i++) {
//            String hex = Integer.toHexString(bytes[i] & 0xFF);
//            if(hex.length() < 2){
//                sb.append(0);
//            }
//            sb.append(hex);
//        }
//        return sb.toString();
//    }

    //    public static void main(String[] args) {
//
//        //创建服务器对象
//        GroupChatServer groupChatServer = new GroupChatServer();
//        groupChatServer.listen();
//    }
    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param bytes 要转换的字节数组
     * @return 转换后的结果
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}


