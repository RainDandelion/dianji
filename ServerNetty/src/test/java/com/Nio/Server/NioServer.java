package com.Nio.Server;

public class NioServer {

    public static void main(String[] args) {
        new Thread(new NioServerHandler(8080)).start();
    }

}
