package com.njrz;

import com.njrz.Netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;

@SpringBootApplication
public class NettyApplication implements CommandLineRunner {

    @Autowired
    private NettyServer nettyServer;

    @Value("${netty.host}")
    public String host;

    @Value("${netty.port}")
    public Integer port;

    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        InetSocketAddress socketAddress = new InetSocketAddress(host,port);
        nettyServer.bind(socketAddress);
    }
}
