package com.example.demo;

import com.example.demo.netty.NettyServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

   @Autowired
   private NettyServer serverNetty;

    @Value("${netty.host}")
    public String host;

    @Value("${netty.port}")
    public Integer port;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        InetSocketAddress socketAddress = new InetSocketAddress(host,port);
        serverNetty.start(socketAddress);
    }
}
