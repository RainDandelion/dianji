package com.nio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * nio客户端，单一线程
 */
@SpringBootApplication

public class NioClientApplication  {
    public static void main(String[] args) {
        SpringApplication.run(NioClientApplication.class,args);
    }

}
