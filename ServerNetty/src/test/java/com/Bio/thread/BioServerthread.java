package com.Bio.thread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServerthread {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            Socket socket = null;

            while(true){
                socket = serverSocket.accept();
                new Thread(new SocketHandler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static class SocketHandler implements Runnable{


        public Socket socket;
        public SocketHandler(Socket socket){
            this.socket = socket;
        }

        public Socket getSocket() {
            return socket;
        }

        public void setSocket(Socket socket) {
            this.socket = socket;
        }

        private InputStream bufferedInputStream;
        private OutputStream bufferedOutputStream;
        @Override
        public void run() {
            try {
                bufferedInputStream = socket.getInputStream();
                bufferedOutputStream = socket.getOutputStream();

                //读操作
                byte[] bytes = new byte[1024];
                int length = 0;
                while ((length = bufferedInputStream.read(bytes)) > 0){
                    System.out.println("input is:" + new String(bytes,0,length));

                    bufferedOutputStream.write("success".getBytes());
                    System.out.println("end");

                }



            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
