package com.nio.client;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    private ServerSocketChannel ListenChannel;
    private Selector selector;
    private static final  int Port = 6667;

    public static void main(String[] args) {
        NioServer server = new NioServer();
        server.listen();
    }


    public NioServer(){
        try {
            ListenChannel = ServerSocketChannel.open();
            selector = Selector.open();
            ListenChannel.socket().bind(new InetSocketAddress("localhost",Port));
            ListenChannel.configureBlocking(false);
            ListenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 监听的方法
     */
    public void listen(){
        while(true){
            try {
                int select = selector.select();
                if(select == 0){
                    System.out.println("目前无事件处理");
                }else {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    SelectionKey selectionKey = null;
                    while(iterator.hasNext()){
                        selectionKey = iterator.next();
                        iterator.remove();  //防范重复处理
                        /**
                         * 感兴趣的
                         */
                        if (selectionKey.isAcceptable()){
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();

                            SocketChannel socketChannel = serverSocketChannel.accept();  //阻塞
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector,SelectionKey.OP_READ);

                            /**
                             * 给出提示，XX上线了
                             */
                            System.out.println(socketChannel.getRemoteAddress() + "上线了");
                        }
                        /**
                         * 通道发生read事件
                         */
                        if (selectionKey.isReadable()){
                            /**
                             * TODO
                             * 转发的时候需要排除自己
                             */
                            readData(selectionKey);
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readData(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            //取到关联的channel
            socketChannel = (SocketChannel)selectionKey.channel();
            //创建buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int read = socketChannel.read(byteBuffer);
            /**
             * 根据read读取的值判断
             */
            if (read > 0){
                //把缓存区数据输出
                String msg = new String(byteBuffer.array());

                System.out.println("input is :" + msg);
                /**
                 * TODO
                 * 向其他客户端转发消息(其他通道)(去掉自己)
                 */
                sendInforToOtherClients(msg,socketChannel);
            }


        } catch (IOException e) {

            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了");
                /**
                 * 离线后： 取消注册
                 *  关闭通道
                 */
                selectionKey.cancel();
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }
    /**
     *
     * @param msg   信息
     * @param self  需要排除的（自己）
     */
    private void sendInforToOtherClients(String msg, SocketChannel self) {
        System.out.println("服务器转发消息中...");
        System.out.println("服务器转发数据给客户端线程: " + Thread.currentThread().getName());
        //遍历 所有注册到selector 上的 SocketChannel,并排除 self
        for(SelectionKey key: selector.keys()) {

            //通过 key  取出对应的 SocketChannel
            Channel targetChannel = key.channel();

            //排除自己
            if(targetChannel instanceof  SocketChannel && targetChannel != self) {

                //转型
                SocketChannel dest = (SocketChannel)targetChannel;
                //将msg 存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer 的数据写入 通道
                try {
                    dest.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        System.out.println("服务器转发消息中");
//        System.out.println("服务器转发数据给客户端线程: " + Thread.currentThread().getName());
//        //遍历所有注册到seletor上的通道，排除自己
//        for (SelectionKey selectionKey : selector.keys()){
//            //通过key取出对应的SocketChannel
//            SocketChannel targetChannel= (SocketChannel)selectionKey.channel();
//            if (targetChannel != self){ //排除自己
//                //将msg存入到buffer
//                ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
//                //将数据写入channel
//                try {
//                    targetChannel.write(wrap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
    }
}
