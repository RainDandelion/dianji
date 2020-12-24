package com.Bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        Socket socket = null;

        try {
            serverSocket = new ServerSocket(5461);
            while(true){
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                byte[] bytes = new byte[1024];

                while(inputStream.read(bytes)> 0){
                    System.out.println(new java.lang.String(bytes));
                }

                outputStream.write(new java.lang.String("Server 收到").getBytes());
                outputStream.flush();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
