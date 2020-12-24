package com.netty.test.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
//    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
//
//            ChannelFuture future = serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
//                public void operationComplete(Future<? super Void> future) {
//                    if (future.isSuccess()) {
//                        System.out.println("端口[" + port + "]绑定成功!");
//                    } else {
//                        System.err.println("端口[" + port + "]绑定失败!");
//                        bind(serverBootstrap, port + 1);
//                    }
//                }
//            });
//            System.out.println("服务器启动成功！ ");
//
//    }
    public static void main(String[] args) {
        int port = 1234;
        EventLoopGroup Boss = new NioEventLoopGroup();
        EventLoopGroup Work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap  = new ServerBootstrap();
        serverBootstrap.group(Boss,Work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel SocketChannel) throws Exception {
                        ChannelPipeline pipeline = SocketChannel.pipeline();
                        pipeline.addLast(new ServerHandler());
                    }
                });
        try {
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            System.out.println("服务器 is ok" );
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            Boss.shutdownGracefully();
            Work.shutdownGracefully();
        }
    }
}
