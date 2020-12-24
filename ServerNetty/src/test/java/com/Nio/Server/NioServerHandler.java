package com.Nio.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioServerHandler implements Runnable{


    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;


    public NioServerHandler(int port){
        try {
            serverSocketChannel = ServerSocketChannel.open();  //大堂经理
            selector = Selector.open();  //服务员
            serverSocketChannel.configureBlocking(false);  //阻塞
            serverSocketChannel.socket().bind(new InetSocketAddress(port));  //酒店开张
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  //感兴趣的事件


        } catch (IOException e) {
            e.printStackTrace();

        }

    }
    @Override
    public void run() {
        while(true){
            try {
                int client = selector.select();

                if(client ==0){
                    System.out.println("当前无连接");
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey selectionKey = null;
                while (iterator.hasNext()){
                    selectionKey = iterator.next();
                    iterator.remove();
                    //判断对事件的感兴趣程度
                    handler(selectionKey);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handler(SelectionKey selectionKey) {

        if (selectionKey.isValid()){
            if (selectionKey.isAcceptable()){
                //进行三次握手...
               ServerSocketChannel channel =  (ServerSocketChannel)selectionKey.channel();
                try {
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (selectionKey.isReadable()){
                //读操作，
                executorService.execute(new AsycHandler(selectionKey));
            }
        }
    }


}
