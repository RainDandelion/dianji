package com.nio.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;

@Service
public class NioClient implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(NioClient.class);


    @Value("${client.Host}")
    private   String Host ;
    @Value("${client.port}")
    private Integer Port;

    private Selector selector;
    private SocketChannel socketChannel;

    @Override
    public  void afterPropertiesSet() throws Exception {
        socketChannel = SocketChannel.open(new InetSocketAddress(Host,Port));
        selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        logger.info("客户端:{} ,准备完成，请求发起连接",socketChannel.getLocalAddress().toString());
        //System.out.println("客户端：" + socketChannel.getLocalAddress().toString() + "is ok!");
    }




    public String SendInfo(String info){
        byte[] bytes = hexStringToByteArray(info);

        try {
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            socketChannel.write(writeBuffer);

            if (!writeBuffer.hasRemaining()) {
                logger.info("写入完成");
               // System.out.println("写入完成");
            }
            String readInfo = readInfo();
            return readInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String readInfo(){
        SelectionKey selectionKey = null;
        try {
            int select = selector.select();
            if (select > 0){

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while(iterator.hasNext()){
                    selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isReadable()){
                        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024 * 10);
                        int read = socketChannel.read(buffer);
                        if (read > 0){
                            buffer.flip();
                            byte[] bytes = new byte[buffer.remaining()];
                            buffer.get(bytes);
                            String body=new String(bytes);
                            return body;
                        }else if (read == -1){

                            selectionKey.cancel();
                            socketChannel.close();
                        }

                    }


                }

            }

        } catch (IOException e) {
            System.out.println("检测到远程连接已关闭");
            try {
                socketChannel.close();
                selectionKey.cancel();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }


    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param hexString 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节，
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }
}
