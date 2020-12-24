package com.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NettyServer {

    private Integer port = 22200;

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    private EventLoopGroup Boss = new NioEventLoopGroup();
    private EventLoopGroup Work = new NioEventLoopGroup();

    private ServerBootstrap serverBootstrap = new ServerBootstrap();


    public void  bind(){
        serverBootstrap.group(Boss,Work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1014)     //设置tcp缓冲区大小
                .option(ChannelOption.SO_SNDBUF, 32*2014)   //设置发送缓冲区大小
                .option(ChannelOption.SO_RCVBUF,32*2014)    //设置接受缓冲区大小
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
//                        ByteBuf delimiter = Unpooled.copiedBuffer("$_$".getBytes());
//                        pipeline.addLast(new DelimiterBasedFrameDecoder(2048,delimiter));

                        pipeline.addLast(new NettyHandler());
                    }
                });
        ChannelFuture sync = null;
        try {
            sync = serverBootstrap.bind(port).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            Boss.shutdownGracefully();
            Work.shutdownGracefully();
        }

    }

}
