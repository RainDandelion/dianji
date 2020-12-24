package com.Bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * BIO的客户端实现
 */
public class BioClient {
    private static  String host = "localhost";
    private static  Integer port = 22200;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input = null;
        Client client = new Client(host, port);
        System.out.println();
        while ((input = "f5 05 02 00 00 09 ff ff ff ff ff ff 12").length() > 0) {
            String res = client.request(input);
            System.out.println("response:" + res);
        }
    }

    static class Client {
        String host;
        int port;

        public Client(String host, int port) {
            this.host = host;
            this.port = port;
            connect();
        }

        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        public boolean connect() {
            if (socket != null && inputStream != null && outputStream != null
                    && socket.isBound() && socket.isConnected() && !socket.isClosed()) {
                return true;
            }
            try {
                socket = new Socket(host,port);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                return true;
            } catch (IOException e) {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
            return false;
        }

        public String request(String request) {
            if (!connect()) {
                return null;
            }
            try {
                outputStream.write(request.getBytes());
                int length = 0;
                byte[] res = new byte[1024];
                while ((length = inputStream.read(res)) > 0) {
                    return new String(res, 0, length);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }
}