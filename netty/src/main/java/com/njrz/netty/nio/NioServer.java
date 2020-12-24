package com.njrz.netty.nio;

public class NioServer {

    public static void main(String[] args) {
        new Thread(new NioServerHandler(8080)).start();
    }

}
