package com.netty.test.client;

import com.netty.test.Packet.MessageRequestPacket;
import com.netty.test.Packet.PacketCodeC;
import com.netty.test.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class NettyClient {
    //    private static void connect(Bootstrap bootstrap, String host, int port) {
//        bootstrap.connect(host, port).addListener(future -> {
//            if (future.isSuccess()) {
//                System.out.println("连接成功!");
//            } else {
//                System.err.println("连接失败，开始重连");
//                connect(bootstrap, host, port);
//            }
//        });
    //   }
    public static void main(String[] args) {
        EventLoopGroup work = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(work)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel SocketChannel) throws Exception {
                        ChannelPipeline pipeline = SocketChannel.pipeline();
                        pipeline.addLast(new ClientHandler());
                    }
                });

        try {
            ChannelFuture sync = bootstrap.connect(new InetSocketAddress("127.0.0.1", 1234)).sync();
            sync.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()){
                        Channel channel = ((ChannelFuture) future).channel();
                        // 连接成功之后，启动控制台线程
                        startConsoleThread(channel);
                    }
                }
            });
            System.out.println("客户端 is" +
                    " ok");
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            work.shutdownGracefully();
        }
    }
    private static void startConsoleThread(Channel channel){
        new Thread(()->{
            while(!Thread.interrupted()){
                if (LoginUtil.hasLogin(channel)){
                    System.out.println("输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();
                    MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
                    messageRequestPacket.setMessage(line);
                    ByteBuf encode = PacketCodeC.INSTANCE.encode(channel.alloc(), messageRequestPacket);
                    channel.writeAndFlush(encode);

                }
            }
        }).start();
    }
}
