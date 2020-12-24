package com.njrz;



import com.njrz.netty3.NettyServer2;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.InetSocketAddress;

//@SpringBootApplication
//public class application implements CommandLineRunner {
//    @Value("${netty.host}")
//    private String host;
//    @Value("${netty.port}")
//    private int port;
////    @Autowired
////    private NettyServer nettyServer;
//
//
//
//    public static void main(String[] args) {
//        ConfigurableApplicationContext run = SpringApplication.run(application.class, args);
//
////        NettyServer bean = run.getBean(NettyServer.class);
////        bean.start();
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
////        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
////        ChannelFuture bing = nettyServer.bing(inetSocketAddress);
////        Runtime.getRuntime().addShutdownHook(new Thread(()->nettyServer.destroy()));
////        bing.channel().closeFuture().syncUninterruptibly();
//
//    }
//}
