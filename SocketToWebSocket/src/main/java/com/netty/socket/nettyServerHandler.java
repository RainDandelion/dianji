package com.netty.socket;

import com.netty.Group.GlobalChannelGroup;
import com.netty.Group.SocketGroup;
import com.netty.utils.WriteToSocketClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.CharsetUtil;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 连接电机
 */
public class nettyServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(nettyServerHandler.class);
//   private ChannelGroup GlobalChannelGroupInstance = GlobalChannelGroup.getINSTANCE();


 

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        logger.info("电机客户端 ：{},加入",ctx.channel().remoteAddress());
        SocketGroup.getInstance().addChannel(ctx.channel().id().asShortText(),ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;
//        byte[] bytes = new byte[byteBuf.readableBytes()];
//        byteBuf.readBytes(bytes);
//
//        System.out.println(new String(bytes, CharsetUtil.UTF_8));


        String temp = (String) msg;
        logger.info("原始报文-------：{}",temp);
        writeToClient(temp,SocketGroup.getInstance().SocketMap,"测试");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("电机客户端：{}",ctx.channel().remoteAddress());
        SocketGroup.getInstance().removeChannel(ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
        ctx.close();
    }
    public static void writeToClient(final String receiveStr, Map<String, Channel> SocketMap, final String mark) {
        try {
            ByteBuf bufff = Unpooled.buffer();//netty需要用ByteBuf传输
            // ConvertCode.hexString2Bytes(receiveStr)
            // Hex.decodeHex(receiveStr.toCharArray())
            bufff.writeBytes(Hex.decodeHex(receiveStr.toCharArray()));//对接需要16进制
            // System.out.println(Hex.decodeHex(receiveStr.toCharArray()));
            // group.writeAndFlush(bufff);
            SocketMap.forEach((id, channel) -> {
                bufff.retain();     //https://www.cnblogs.com/lyzj/p/13207189.html
                channel.writeAndFlush(bufff)
                        .addListeners(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                StringBuilder sb = new StringBuilder("");
                                if(!StringUtils.isEmpty(mark)){
                                    sb.append("【").append(mark).append("】");
                                }
                                if (future.isSuccess()) {

                                    logger.info(sb.toString()+"回写成功"+receiveStr);
                                } else {

                                    logger.error(sb.toString()+"回写失败"+receiveStr);
                                }
                            }

                        });
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("调用通用writeToClient()异常"+e.getMessage());
        }
    }
}
