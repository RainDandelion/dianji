package com.njrz.netty.nettyServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
public class NettyServer {

    private static Logger logger = LoggerFactory.getLogger(NettyServer.class);
    @Value("${netty.host}")
    private String HOST;

    @Value("${netty.port}")
    private Integer port;

    private EventLoopGroup BOSS;

    private EventLoopGroup Work;

    private ServerBootstrap serverBootstrap = new ServerBootstrap();
    public void bind(){
        BOSS = new NioEventLoopGroup();
        Work = new NioEventLoopGroup();

        serverBootstrap.group(BOSS,Work)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY,true)

                .childHandler(new MyChannelInitializer());
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(HOST, port);
            ChannelFuture sync = serverBootstrap.bind(socketAddress).sync();
            logger.info("服务器端启动{}，开始监听端口{}",socketAddress.getAddress(),socketAddress.getPort() );
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            BOSS.shutdownGracefully();
            Work.shutdownGracefully();
        }
    }
    @PreDestroy
    public void destory(){
        BOSS.shutdownGracefully().syncUninterruptibly();
        Work.shutdownGracefully().syncUninterruptibly();
        logger.info("netty服务端推出");

    }
}
