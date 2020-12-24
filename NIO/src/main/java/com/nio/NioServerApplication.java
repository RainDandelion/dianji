package com.nio;

import com.nio.Server.GroupChatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NIO服务端 多线程
 */
@SpringBootApplication
public class NioServerApplication implements CommandLineRunner {

    @Autowired
    private GroupChatServer server;

    public static void main(String[] args) {
        SpringApplication.run(NioServerApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        server.listen();
    }
}
