package com.ztguigu.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServer {
    public static void main(String[] args) {


        EventLoopGroup boss= new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();



        serverBootstrap.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        System.out.println("客户 socketChannel  的hashcode" + socketChannel.hashCode());  //可以使用集合，，推送任务时，可以将业务加入到各个channel，对应的
                        //NioEventloop的tasequeue或者scheduleTackQueue
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new ServerHandler());
                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind(6666).sync();
            //注册监听器
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()){
                        System.out.println("监听端口 6666 成功");
                    }else {
                        System.out.println("监听 6666 失败");
                    }
                }
            });


            System.out.println("服务器 is ready....");
            future.channel().closeFuture().sync();  //对关闭通道进行监听  --》异步请求
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }

    }

}
