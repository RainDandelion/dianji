package com.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NioClient {
    private SocketChannel socketChannel;
    private final String host = "127.0.0.1";
    private final Integer port = 6667;

    private Selector selector;

    private String username;

    /**
     * 初始化
     */
    public NioClient(){
        try {
            selector = Selector.open();
            //连接服务器
            socketChannel = socketChannel.open(new InetSocketAddress("127.0.0.1", port));
            //设置非阻塞
            socketChannel.configureBlocking(false);
            //将channel 注册到selector
            socketChannel.register(selector, SelectionKey.OP_READ);
            //得到username
            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username + " is ok...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 向服务器发生消息
     */
    public void sendInfo(String msg){
          msg =  "我说：" + msg;

        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    /**
     * 从服务器中读取消息
     */
    public void readInfo(){
        try {
            int read = selector.select();
            if (read > 0){
                //有事件通道，

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if(selectionKey.isReadable()){
                        //得到相关的通道
                        SocketChannel sc = (SocketChannel) selectionKey.channel();
                        ByteBuffer allocate = ByteBuffer.allocate(1024);

                        sc.read(allocate);
                        String msg = new String(allocate.array());

                        System.out.println(msg);
                    }
                }

            }else{
                //System.out.println(" 没有可用的通道"
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        NioClient client = new NioClient();
        new Thread(()->{
            while(true){
                client.readInfo();
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String s = scanner.nextLine();
            client.sendInfo(s);
        }
    }

}
