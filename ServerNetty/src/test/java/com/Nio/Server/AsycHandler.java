package com.Nio.Server;



import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class AsycHandler implements Runnable{

    private SocketChannel socketChannel;
    private SelectionKey selectionKey;


    public AsycHandler(SelectionKey selectionKey){
        this.selectionKey = selectionKey;
    }
    @Override
    public void run() {
       socketChannel =  (SocketChannel)selectionKey.channel();
       //读操作

        ByteBuffer readBuffer = ByteBuffer.allocate(1024 * 4);

        try {
            int read = socketChannel.read(readBuffer);

            if(read > 0){

                Thread.sleep(100);

                readBuffer.flip();

                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);
                String body = new String(bytes, "UTF-8");
                System.out.println("input is:" + body);

                res(socketChannel, body);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    //写操作
    private void res(SocketChannel socketChannel, String body) {
        byte[] bytes = body.getBytes();

        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();

        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }





}
