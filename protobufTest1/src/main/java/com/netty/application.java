package com.netty;

import com.netty.client.SubReqProClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class application implements CommandLineRunner {

    @Autowired
    private SubReqProClient subReqProClient;

    public static void main(String[] args) {
        SpringApplication.run(application.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        int port=15444;

        subReqProClient.bind(port, "localhost");
    }
}
