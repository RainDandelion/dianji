package com.njrz.netty.nettyServer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
       // channelPipeline.addLast(new MyDecoder());
        // HttpServerCodec：将请求和应答消息解码为HTTP消息
        channelPipeline.addLast(new HttpServerCodec());
        // HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
        channelPipeline.addLast(new HttpObjectAggregator(65536));
        // ChunkedWriteHandler：向客户端发送HTML5文件
        channelPipeline.addLast(new ChunkedWriteHandler());
        channelPipeline.addLast(new LineBasedFrameDecoder(1024));
//        channelPipeline.addLast(new StringDecoder(Charset.forName("GBK")));
////        channelPipeline.addLast(new StringEncoder(Charset.forName("GBK")));
      //  channelPipeline.addLast(new MyDecoder());
        channelPipeline.addLast(new MyServerInputHandler());
        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws",true));
        channelPipeline.addLast(new HttpObjectAggregator(1024 * 64));
        channelPipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));  //心跳检测

    }
}
