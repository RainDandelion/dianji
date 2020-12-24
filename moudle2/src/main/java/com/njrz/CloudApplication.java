package com.njrz;

import com.njrz.netty3.NettyServer2;
import com.njrz.netty4.server.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;

@SpringBootApplication
public class CloudApplication implements CommandLineRunner {
    @Value("${netty.host}")
    private String host;
    @Value("${netty.port}")
    private int port;
//    @Autowired
//    private NettyServer2 nettyServer;

    @Autowired
    private NettyServer server;
    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }

    @Override
    public void run(String... strings) {
        //nettyServer.start(new InetSocketAddress(host,port));
        server.start(new InetSocketAddress(host,port));
    }
}