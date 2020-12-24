package com.Bio.ThreadPool.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServerPool {
    public static void main(String[] args) {
        int port = 22200;
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(port);
            ServerHandlerExecutePool executePool = new ServerHandlerExecutePool(50,100);
            while(true){
                socket = serverSocket.accept();

                executePool.execute(new SocketHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket !=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
