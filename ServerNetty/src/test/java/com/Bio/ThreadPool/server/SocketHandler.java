package com.Bio.ThreadPool.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketHandler implements Runnable{

    private Socket socket;
    private InputStream bufferedInputStream;
    private OutputStream bufferedOutputStream;

    public SocketHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            System.out.println("线程开启：" + Thread.currentThread().getName());
            bufferedInputStream = socket.getInputStream();
            bufferedOutputStream = socket.getOutputStream();
            byte[] bytes = new byte[102400];
            int length = 0;
            while((length = bufferedInputStream.read(bytes)) >0){
                System.out.println("input is :" + new String(bytes,0,length));


                bufferedOutputStream.write("success".getBytes());
              

            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedOutputStream != null){
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedInputStream !=null){
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
