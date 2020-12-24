package com.netty.WebSocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 接收网页的信息
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast(new HttpServerCodec());
        channelPipeline.addLast(new ChunkedWriteHandler());
        channelPipeline.addLast("input",new nettyWebServerHandler());
        channelPipeline.addLast(new HttpObjectAggregator(1024 * 64));
        channelPipeline.addLast(new LineBasedFrameDecoder(1024));  //解决tcp粘包问题
        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws",true));
        channelPipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));
    }
}
