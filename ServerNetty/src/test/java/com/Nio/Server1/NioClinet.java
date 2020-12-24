package com.Nio.Server1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClinet {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress socketAddress = new InetSocketAddress("localhost", 8080);

        if (!socketChannel.connect(socketAddress)){
            while(!socketChannel.finishConnect()){
                System.out.println("因连接需要时间，客户端不会阻塞，可以做其他工作");
            }
        }
        //链接成功，发送数据
        String S = "String hello";

        ByteBuffer byteBuffer =ByteBuffer.wrap(S.getBytes()); //放入

        byteBuffer.flip();
        socketChannel.write(byteBuffer);

        //暂停
        System.in.read();



    }
}
