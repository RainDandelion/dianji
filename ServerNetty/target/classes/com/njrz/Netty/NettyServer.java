package com.njrz.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
public class NettyServer {
    private Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private EventLoopGroup Boss = new NioEventLoopGroup();  //大堂经理
    private EventLoopGroup work = new NioEventLoopGroup();  //服务员

    public void bind(InetSocketAddress inetSocketAddress){
        ServerBootstrap serverBootstrap =new ServerBootstrap();  //酒店

        serverBootstrap.group(Boss,work)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .channel(NioServerSocketChannel.class)
                .childHandler(new MyChannelInitializer());

        try {
            ChannelFuture future = serverBootstrap.bind(inetSocketAddress).sync();
            logger.info("服务器端启动{}，开始监听端口{}",inetSocketAddress.getAddress(),inetSocketAddress.getPort() );
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            Boss.shutdownGracefully();
            work.shutdownGracefully();
        }

    }

    @PreDestroy
    public void destory(){
        Boss.shutdownGracefully().syncUninterruptibly();
        work.shutdownGracefully().syncUninterruptibly();
        logger.info("netty服务端推出");

    }

}
