package com.Nio.Server1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws IOException {
        //buffer  --》 channel  --》 buffer
        //程序对和buffer进行交互

        //Selector  选择器  可注册多个通道

        //每个channel都对于一个buffer，selector对应一个线程，一个线程对应多个channel
        //程序切换到那个channel是有事件决定的，
        //selector会根据不同的事件，在各个channel中切换

        //buffer是一个内存块，底层是数组


        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();

        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);



        while (true){
            if (selector.select(1000) == 0){
                System.out.println("没有事件发生");
                continue;
            }

            SelectionKey selectionKey = null;
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isAcceptable()){
                    /**
                     * 有客户端连接进入
                     *
                     */
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    /**
                     * 关注事件，同时给socketChannel关联一个buffer
                     */
                    socketChannel.register(selector,SelectionKey.OP_READ,ByteBuffer.allocate(1024));

                }
                if (selectionKey.isReadable()){
                    //通过key反向获取对应channel；
                    SocketChannel socketChannel =  (SocketChannel)selectionKey.channel();

                    //获取该channel关联的buffer
                    ByteBuffer byteBuffer = (ByteBuffer)selectionKey.attachment();

                    socketChannel.read(byteBuffer);
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    System.out.println("from 客户端 ：" + new String(byteBuffer.array()));
                    byteBuffer.flip();

                }
            }
        }





    }
}
