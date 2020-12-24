package com.njrz.netty;

import com.fasterxml.jackson.core.ObjectCodec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.logging.Logger;
//
//@Component
//public class NettyServerListener {
//
//    private static  final Logger LOGGER = (Logger) LoggerFactory.getLogger(NettyServerListener.class);
//
//    ServerBootstrap serverBootstrap = new ServerBootstrap();
//
//    EventLoopGroup boss = new NioEventLoopGroup();
//
//    EventLoopGroup work = new NioEventLoopGroup();
//
//
//
//    @Resource
//    private ServerChannelHandlerAdapter channelHandlerAdapter;
//
//    @Resource
//    private NettyConfig nettyConfig;
//
//
//    @PreDestroy
//    public void close(){
//        LOGGER.info("关闭服务器");
//
//        boss.shutdownGracefully();
//        work.shutdownGracefully();
//    }
//
//    public void start() {
//        // 从配置文件中(application.yml)获取服务端监听端口号
//        int port = nettyConfig.getPort();
//        serverBootstrap.group(boss, work)
//                .channel(NioServerSocketChannel.class)
//                .option(ChannelOption.SO_BACKLOG, 100)
//                .handler(new LoggingHandler(LogLevel.INFO));
//        try {
//            //设置事件处理
//            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                protected void initChannel(SocketChannel ch) throws Exception {
//                    ChannelPipeline pipeline = ch.pipeline();
//                    pipeline.addLast(new LengthFieldBasedFrameDecoder(nettyConfig.getMaxFrameLength()
//                            , 0, 2, 0, 2));
//                    pipeline.addLast(new LengthFieldPrepender(2));
//                    pipeline.addLast(new ObjectCodec());
//
//                    pipeline.addLast(channelHandlerAdapter);
//                }
//            });
//            LOGGER.info("netty服务器在[{}]端口启动监听", port);
//            ChannelFuture f = serverBootstrap.bind(port).sync();
//            f.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            LOGGER.info("[出现异常] 释放资源");
//            boss.shutdownGracefully();
//            work.shutdownGracefully();
//        }
//    }
//}
