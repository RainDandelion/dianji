package com.njrz;

import com.njrz.netty.BootNettyServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BootNettyApplication implements CommandLineRunner {
    public static void main( String[] args )
    {
        /**
         * 启动springboot
         */
        SpringApplication app = new SpringApplication(BootNettyApplication.class);
        //app.setWebApplicationType(WebApplicationType.NONE);//不启动web服务
        app.run(args);

        System.out.println( "Hello World!" );
    }

    @Async
    @Override
    public void run(String... args) throws Exception {
        /**
         * 使用异步注解方式启动netty服务端服务
         */
        new BootNettyServer().bind(8888);

    }
}