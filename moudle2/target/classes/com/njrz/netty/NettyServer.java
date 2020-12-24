package com.njrz.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
//
//@Component
//public class NettyServer {
//    @Value("${server.port}")
//    private int port;
//
//    @PostConstruct
//    public void start(){
//        NioEventLoopGroup BossGroup = new NioEventLoopGroup();
//        NioEventLoopGroup group = new NioEventLoopGroup();
//
//        ServerBootstrap sb = new ServerBootstrap();
//        sb.option(ChannelOption.SO_BACKLOG,1024);
//        sb.group(group,BossGroup)
//                .channel(NioServerSocketChannel.class)
//                .localAddress(this.port)
//                .childHandler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel socketChannel) throws Exception {
//                        System.out.println("收到新连接:"+socketChannel.localAddress());
//                        socketChannel.pipeline().addLast(new HttpServerCodec());
//                        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
//                        socketChannel.pipeline().addLast(new HttpObjectAggregator(8192));
//                        socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/ws", "WebSocket", true, 65536 * 10));
//                        socketChannel.pipeline().addLast(new MyWebSocketHandler());
//                    }
//                });
//        try {
//            ChannelFuture channelFuture = sb.bind(port).sync();
//            channelFuture.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                group.shutdownGracefully().sync();
//                BossGroup.shutdownGracefully().sync();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//}
