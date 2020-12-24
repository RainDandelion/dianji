package com.netty;

import com.netty.Server.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DianjiNettyServerApplication implements CommandLineRunner {

    @Autowired
    private NettyServer nettyServer;
    public static void main(String[] args) {
        SpringApplication.run(DianjiNettyServerApplication.class,args);
    }


    @Override
    public void run(String... args) throws Exception {
        nettyServer.bind();
    }
}
