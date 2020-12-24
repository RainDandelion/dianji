package com.netty.client;

import com.netty.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * 源码学院-ant
 * 只为培养BAT程序员而生
 * http://bat.ke.qq.com
 * 往期视频加群:516212256 暗号:6
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);


    /**
     * 通道被激活触发的操作（链接成功后,写入数据）
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes("f5 05 02 01 00 02 ff ff ff ff ff ff 12".getBytes());
       // ByteBuf outBuffer = Unpooled.copiedBuffer("f5 05 02 01 00 02 ff ff ff ff ff ff 12", CharsetUtil.UTF_8);
        ctx.writeAndFlush(byteBuf);
    }

    /**
     * 读到数据事触发的方法
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
//        System.out.println("accept msg:" + msg.toString());
//    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

//ByteUtil.BinaryToHexString(bytes)
        logger.info("来自客户端的消息：{}", new String(bytes));
        //ByteBuf outBuffer = Unpooled.copiedBuffer("hi netty client", CharsetUtil.UTF_8);
        //会出现死循环
//        ByteBuf outBuffer = Unpooled.copiedBuffer(new String(bytes), CharsetUtil.UTF_8);
//        ctx.writeAndFlush(outBuffer);
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
