package com.Bio;

import java.io.IOException;
import java.net.Socket;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        // 通过构造函数创建Socket，并且连接指定地址和端口的服务端
        try {
            Socket socket = new Socket("localhost", 22200);
            System.out.println("请输入信息");
            new ReadMsg(socket).start();
            PrintWriter pw = null;
            // 写数据到服务端
            while (true) {
                pw = new PrintWriter(socket.getOutputStream());
                pw.println(new Scanner(System.in).next());
                pw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static class ReadMsg extends Thread {
        Socket socket;

        public ReadMsg(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String line = null;
                // 通过输入流读取服务端传输的数据
                while ((line = br.readLine()) != null) {
                    System.out.printf("%s\n", line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
