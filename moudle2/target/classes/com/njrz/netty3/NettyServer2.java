package com.njrz.netty3;

import com.njrz.netty2.MyChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
public class NettyServer2 {



   private NioEventLoopGroup Boss = new NioEventLoopGroup(); //大堂经理
   private NioEventLoopGroup group = new NioEventLoopGroup(); //服务员


    public void start(InetSocketAddress inetSocketAddress) {

        try {
            ServerBootstrap sb = new ServerBootstrap();  //酒店
            sb.group(Boss, group)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new MyChannelInitializer2());

            ChannelFuture cf = sb.bind(inetSocketAddress).sync();
            System.out.println("服务器启动开始监听端口: " + inetSocketAddress.getAddress() + "+" + inetSocketAddress.getPort());
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            Boss.shutdownGracefully();
        }
    }

    @PreDestroy
    public void destroy() {
        Boss.shutdownGracefully().syncUninterruptibly();
        group.shutdownGracefully().syncUninterruptibly();
        System.out.println("关闭 Netty 成功");
    }
}
