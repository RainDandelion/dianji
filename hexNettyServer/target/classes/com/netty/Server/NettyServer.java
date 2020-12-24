package com.netty.Server;

import com.netty.codec.HexDecode;
import com.netty.handler.TcpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Service;

@Service
public class NettyServer {

    private EventLoopGroup Boss= new NioEventLoopGroup();
    private EventLoopGroup Work= new NioEventLoopGroup();

    private ChannelFuture channelFuture = null;


    private static Integer port = 1234;
    private ServerBootstrap serverBootstrap = new ServerBootstrap();

    public  void bind(){
        serverBootstrap.group(Boss,Work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                       // pipeline.addLast(new HexDecode());

                        pipeline.addLast(new TcpHandler());

                        pipeline.addLast(new StringEncoder());
                    }
                });
        try {
            channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("服务端开启");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}
