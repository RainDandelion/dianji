package com.example.demo.netty;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;


@Component
public class NettyServer {
    public void start(InetSocketAddress socketAddress) {
        EventLoopGroup BossGroup = new NioEventLoopGroup();  //线程组，负责accept事件  --》大堂经理
        EventLoopGroup WorkGroup = new NioEventLoopGroup();  // 线程组，负责读写事件 --》服务员

        ServerBootstrap sb = new ServerBootstrap();  //引导类   --》酒店  当前什么都没有
        sb.group(BossGroup,WorkGroup)  //主从模型切换
                .channel(NioServerSocketChannel.class)  //通讯模式
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline channelPipeline=socketChannel.pipeline();
                        // HttpServerCodec：将请求和应答消息解码为HTTP消息
                        channelPipeline.addLast(new HttpServerCodec());
                        // HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
                        channelPipeline.addLast(new HttpObjectAggregator(65536));
                        // ChunkedWriteHandler：向客户端发送HTML5文件
                        channelPipeline.addLast(new ChunkedWriteHandler());
                        channelPipeline.addLast(new LineBasedFrameDecoder(1024));
                        channelPipeline.addLast(new StringDecoder(Charset.forName("GBK")));
                        channelPipeline.addLast(new StringEncoder(Charset.forName("GBK")));
                        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws",true));
                        channelPipeline.addLast(new HttpObjectAggregator(1024 * 64));
                        channelPipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));  //心跳检测
                        channelPipeline.addLast(new NettyServerHandler());

                    }
                });
        try {

            ChannelFuture sync = sb.bind(socketAddress).sync();

            sync.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //优雅关闭
            BossGroup.shutdownGracefully();
            WorkGroup.shutdownGracefully();
        }
    }

//    public static void main(String[] args) throws InterruptedException {
//        new NettyServer().bind(8080);
//    }
}