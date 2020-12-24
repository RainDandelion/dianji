package com.netty.Server;


import com.netty.coder.HexDecode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Component
public class NettyServer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private EventLoopGroup Boss = new NioEventLoopGroup();
    private EventLoopGroup Work = new NioEventLoopGroup();

    private Integer port = 22200;
    private ServerBootstrap serverBootstrap;



    public void bind(){
        try {
            serverBootstrap = new ServerBootstrap();
            NettyServerHandler nettyServerHandler = new NettyServerHandler();
            serverBootstrap.group(Boss,Work)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            //channelPipeline.addLast(new HexDecode());
//                            channelPipeline.addLast(new HttpServerCodec());
//                            channelPipeline.addLast(new ChunkedWriteHandler());
                           // channelPipeline.addLast(new StringDecoder());
                            channelPipeline.addLast("input",nettyServerHandler);
                            channelPipeline.addLast(new HttpObjectAggregator(1024 * 64));
                          //  channelPipeline.addLast(businessGroup,new MyBusinessHandler());
                           channelPipeline.addLast(new LineBasedFrameDecoder(1024));  //解决tcp粘包问题
                            channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws",true));

                            channelPipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));  //心跳检测
                        }
                    });
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            logger.info("服务端启动完成，正在监听：{}",sync.channel().localAddress() );
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            Boss.shutdownGracefully();
            Work.shutdownGracefully();

        }
    }
}
