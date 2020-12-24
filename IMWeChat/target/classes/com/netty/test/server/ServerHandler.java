package com.netty.test.server;

import com.netty.test.Packet.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf requestByteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.INSTANCE.decode(requestByteBuf);

        if (packet instanceof LoginRequestPacket) {
            System.out.println(new Date() + ": 收到客户端登录请求……");
            // 登录流程
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;

            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());
            if (valid(loginRequestPacket)) {
                loginResponsePacket.setSuccess(true);
                System.out.println(new Date() + ": 登录成功!");
            } else {
                loginResponsePacket.setReason("账号密码校验失败");
                loginResponsePacket.setSuccess(false);
                System.out.println(new Date() + ": 登录失败!");
            }
            // 登录响应
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        } else if (packet instanceof MessageRequestPacket) {
            // 客户端发来消息
            MessageRequestPacket messageRequestPacket = ((MessageRequestPacket) packet);

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());
            messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}

//public class ServerHandler extends ChannelInboundHandlerAdapter {
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;
//
//
//        Packet decode = PacketCodeC.INSTANCE.decode(byteBuf);
//
//
//        if (decode instanceof LoginRequestPacket){
//            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) decode;
//
//
//            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
//            if (valid(loginRequestPacket)){
//                loginResponsePacket.setSuccess(true);
//                System.out.println(new Date() + ": 登录成功!");
//            }else {
//                loginResponsePacket.setReason("账号密码校验失败");
//                loginResponsePacket.setSuccess(false);
//                System.out.println(new Date() + ": 登录失败!");
//            }
//            ByteBuf encode = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
//            ctx.channel().writeAndFlush(encode);
//        }else if (decode instanceof MessageRequestPacket){
//            MessageRequestPacket messageRequestPacket = (MessageRequestPacket) decode;
//            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());
//
//            //返回信息
//            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
//            messageRequestPacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
//            ByteBuf encode = PacketCodeC.INSTANCE.encode(ctx.alloc(), messageResponsePacket);
//
//            ctx.channel().writeAndFlush(encode);
//        }
//
//
//
//    }
//    private boolean valid(LoginRequestPacket loginRequestPacket) {
//        return true;
//    }
//}
