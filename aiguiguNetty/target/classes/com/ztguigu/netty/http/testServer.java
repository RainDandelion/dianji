package com.ztguigu.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class testServer {
    public static void main(String[] args) {
        EventLoopGroup boss= new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new testServerInitializer());

        try {
            ChannelFuture future = serverBootstrap.bind(2020).sync();
//            future.addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                    if (future.isSuccess()){
//                        System.out.println("服务器监听端口成功");
//                    }else {
//                        System.out.println("服务器监听端口失败");
//                    }
//
//                }
//            });
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }

    }


}
