package com.netty.WebSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netty.Group.GlobalChannelGroup;
import com.netty.Group.GlobalWebChannelGroup;
import com.netty.Group.SocketGroup;
import com.netty.Group.WebSocketGroup;
import com.netty.entity.UserInfo;
import com.netty.utils.ConvertCode;
import com.netty.utils.WriteToSocketClient;
import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class nettyWebServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private Logger logger = LoggerFactory.getLogger(nettyWebServerHandler.class);
//    private static Map<String,Channel> map = new ConcurrentHashMap<>();
//    private ChannelGroup GlobalChannelGroupInstance = GlobalChannelGroup.getINSTANCE();  //电机客户端
    private byte dirleft =0;
    private byte dirright =1;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //.replace(" ","")
        String message = msg.text();
        JSONObject userJson = JSONObject.parseObject(message);
        UserInfo userInfo = JSON.toJavaObject(userJson, UserInfo.class);
//        System.out.println(userInfo.getId());
//        System.out.println(userInfo.getDir());

//        /**
//         * TODO
//         * 移掉 ch的唯一值，将用户端id重新设置上去.
//         */
//        map.remove(ctx.channel().id().asLongText());
//        map.put(userInfo.getId(),ctx.channel());
//        //System.out.println(map.toString());
//        /**
//         * 获取移动的步伐
//         */
//        byte dir = userInfo.getDir();
//        logger.info("当前连接数: {}",map.size());
//        logger.info("原始报文-------：{}",message);
//
//        byte[] bytes = new byte[]{(byte) 0xf5,0x05,0x02,1,0,2, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,0x10};
//        if (dir == 1){
//            bytes[3] = this.dirleft;
//        }else {
//            bytes[3] = this.dirright;
//        }
//        System.out.println(GlobalChannelGroupInstance.toArray());
//        GlobalChannelGroupInstance.forEach(channel -> {
//            System.out.println(channel.toString());
//            channel.writeAndFlush(Unpooled.copiedBuffer(bytes));
//        });
        /**
         * TODO
         * 移掉 ch的唯一值，将用户端id重新设置上去.
         */
        // WebSocketGroup.getInstance().replaceId(ctx.channel().id().asLongText(),userInfo.getId());
        WebSocketGroup.getInstance().replaceId(ctx.channel(),userInfo.getId());
        /**
         * 获取移动的步伐
         */
        byte dir = userInfo.getDir();
        logger.info("当前连接数: {}",WebSocketGroup.getInstance().CountWebChannel());
        logger.info("原始报文-------：{}",message);

        byte[] bytes = new byte[]{(byte) 0xf5,0x05,0x02,1,0,2, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,0x10};
        if (dir == 1){
            bytes[3] = this.dirleft;
        }else {
            bytes[3] = this.dirright;
        }

        SocketGroup.getInstance().sendMessage(bytes);
//        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer(500);
//        byteBuf.writeBytes(bytes);
       // GlobalChannelGroupInstance.writeAndFlush(Unpooled.copiedBuffer(bytes));
//        GlobalWebChannelGroupInstance.forEach(o->{
//                //socket
////            String string = ConvertCode.string2HexString(message);
////            System.out.println(string);
//            WriteToSocketClient.writeToClient(message,GlobalChannelGroupInstance,"测试");
//                //websocket
//            TextWebSocketFrame text1 = new TextWebSocketFrame(o.remoteAddress() + "发送消息：" + message + "\n");
//            o.writeAndFlush(text1);
//
//        });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel ch = ctx.channel();
        //map.put(ch.id().asLongText(),ch);
        WebSocketGroup.getInstance().AddChannel(ch.id().asLongText(),ch);
       // System.out.println(map.toString());
        //GlobalWebChannelGroupInstance.add(ch);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        logger.info(ctx.channel().remoteAddress()+":离开");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel ch = ctx.channel();
        logger.info(ch.remoteAddress()+"：连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
        ctx.close();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        WebSocketGroup.getInstance().removeChannel(ctx.channel());
///**
// * 根据值 删除key-value
// */
//        Collection<Channel> values = map.values();
//        while (true == values.contains(ctx.channel())){
//            values.remove(ctx.channel());
//        }

    }
}
