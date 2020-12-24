package com.Bio.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {

    private static  ExecutorService service = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        ServerSocket serverSocket;

        {
            try {
                System.out.println("线程id = " + Thread.currentThread().getId()  + "          name" +  Thread.currentThread().getName());
                serverSocket = new ServerSocket(6666);
                System.out.println("服务器启动");
                while(true){
                    System.out.println("等待连接");
                    Socket socket = serverSocket.accept();


                    System.out.println("连接到一个客户端");

                    service.execute(() ->{
                        handler(socket);
                    });

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  static void handler(Socket socket){

        try {
            System.out.println("线程id = " + Thread.currentThread().getId()  + "          name" +  Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream=  socket.getOutputStream();

            int length = 0;

            while((length = inputStream.read(bytes)) !=0){  //阻塞
                System.out.println(new String(bytes,0,length));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
