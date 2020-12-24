package com.njrz.netty4.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.Date;

@ChannelHandler.Sharable  //该handler能在多个线程间共享， 实现必须要是线程安全的
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {
    public NettyServerHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame)o;
        String req = textWebSocketFrame.text();
        String date = new Date().toString();

        channelHandlerContext.writeAndFlush(new TextWebSocketFrame("现在时刻:"+date+"发送了:"+req));
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf)msg;
//
//        byte[] bytes = new byte[byteBuf.readableBytes()];
//
//        byteBuf.readBytes(bytes);
//
//        System.out.println("server accept:" + new String(bytes, CharsetUtil.UTF_8));
//        ByteBuf out = Unpooled.copiedBuffer("hi netty client", CharsetUtil.UTF_8);
//        ctx.writeAndFlush(out);
//    }


}
