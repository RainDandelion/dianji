package com.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GenericFutureListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class NettyClient {

    private EventLoopGroup work = new NioEventLoopGroup();
    private Bootstrap bootstrap;

    private ChannelFuture channelFuture;
    private String Host = "localhost";
    @Value("${netty.port}")
    private Integer Port;

    @PostConstruct
    public void Start() throws Exception {
        EventExecutorGroup businessGroup = new DefaultEventExecutorGroup(100);  //自定义线程池


        bootstrap = new Bootstrap();
        bootstrap.group(work)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new NettyClientHandler());
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new ChunkedWriteHandler());
                        pipeline.addLast(new HttpObjectAggregator(1024 * 64));
                        //  channelPipeline.addLast(businessGroup,new MyBusinessHandler());
                        pipeline.addLast(new LineBasedFrameDecoder(1024));  //解决tcp粘包问题
                      //  pipeline.addLast(businessGroup,new MyBusinessHandler());  //业务流程
                    }
                });
        channelFuture = bootstrap.connect(new InetSocketAddress(Host, Port)).sync();
        //断线重连
//        channelFuture.addListener((ChannelFutureListener) future -> {
//                if (future.isSuccess()){
//                    System.out.println("客户端启动完成");
//                }else {
//                    System.out.println("服务器启动失败");
//                    future.channel().eventLoop().schedule(()-> {
//                        try {
//                            Start();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    },20, TimeUnit.SECONDS);
//                }
//
//        });


        channelFuture.channel().closeFuture().sync();
    }
}
