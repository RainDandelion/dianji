package com.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
@ChannelHandler.Sharable
public class NettyHandler extends ChannelInboundHandlerAdapter {
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

@Override
    public void channelRead(ChannelHandlerContext ctx,Object msg)
    throws  Exception{
        ByteBuf byteBuf=(ByteBuf)msg;
        byte[] bytes=new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        System.out.println("Server Accept:"+BinaryToHexString(bytes));

        //ByteBuf outBuffer = Unpooled.copiedBuffer(new String(bytes,CharsetUtil.UTF_8), CharsetUtil.UTF_8);
        clients.writeAndFlush(Unpooled.buffer().writeBytes(bytes));
    }
    protected NettyHandler() {
        super();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
       clients.add(ctx.channel());
        System.out.println("有一客户端加入" + ctx.channel().remoteAddress());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有一客户端离开");
    }
    //将字节数组转换为16进制字符串
    public static String BinaryToHexString(byte[] bytes) {
        String hexStr = "0123456789abcdef";
        String result = "";
        String hex = "";
        for (byte b : bytes) {
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
            hex += String.valueOf(hexStr.charAt(b & 0x0F));
            result += hex + " ";
        }
        return result;
    }
}
