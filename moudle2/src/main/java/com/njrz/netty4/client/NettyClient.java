package com.njrz.netty4.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;


public class NettyClient {

    private ChannelFuture f;


    public void connect(String host,int port){
        EventLoopGroup group = new NioEventLoopGroup();  //点菜

        Bootstrap bootstrap = new Bootstrap();  //客户端启动程序

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
        try {
            f =bootstrap.connect(host,port).sync(); /* 连接到远程节点，阻塞等待直到连接完成*/

            f.addListeners(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    ByteBuf out = Unpooled.copiedBuffer("hi netty server", CharsetUtil.UTF_8);
                    f.channel().writeAndFlush(out);
                }
            });
            System.out.println("连接成功");
            f.channel().closeFuture().sync();  //因为finally中shutdown了 所以让当前线程阻塞，否则无法读了。
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        int port = 8080;
        NettyClient client = new NettyClient();
        client.connect("localhost",port);
    }
}
