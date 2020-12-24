package com.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

@Service
public class NettyClient implements InitializingBean {

    private ChannelFuture channelFuture;
    private String Host;
    private Integer Port;

    private EventLoopGroup Work = new NioEventLoopGroup();

    @Override
    public void afterPropertiesSet()  {

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(Work)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .channel(NioServerSocketChannel.class)
                    .handler(new ClientChannelInitializer());

            channelFuture =  bootstrap.bind(new InetSocketAddress(Host,Port)).sync();
            System.out.println("客户端 启动" );
            channelFuture.syncUninterruptibly().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            Work.shutdownGracefully();
        }
    }
}
