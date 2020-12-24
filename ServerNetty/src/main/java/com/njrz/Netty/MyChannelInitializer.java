package com.njrz.Netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
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
        channelPipeline.addLast(new MyServerHandler());
        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws",true));
        channelPipeline.addLast(new HttpObjectAggregator(1024 * 64));
        channelPipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));  //心跳检测
//        /**
//         * http-request解码器
//         * http服务器端对request解码
//         */
//        channelPipeline.addLast("decoder", new HttpRequestDecoder());
//        /**
//         * http-response解码器
//         * http服务器端对response编码
//         */
//        channelPipeline.addLast("encoder", new HttpResponseEncoder());

    }
}
