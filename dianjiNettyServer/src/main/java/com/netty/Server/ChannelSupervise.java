package com.netty.Server;

import com.netty.utils.ByteUtil;
import com.netty.utils.ConvertCode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelSupervise {
    private static Logger logger = LoggerFactory.getLogger(ChannelSupervise.class);
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static Map<String, ChannelId> channelIdMap = new ConcurrentHashMap<>();

    public static void addChannel(Channel channel){
        clients.add(channel);
        channelIdMap.put(channel.id().asLongText(),channel.id());
    }

    public static void removeChannel(Channel channel){
        clients.remove(channel);
        channelIdMap.remove(channel.id().asLongText());
        logger.info("客户端连接断开  ——   channel id : {}", channel.id().asLongText());
        logger.info("剩余客户端：{}", clients.size());
    }

    public static Channel findChannel(String id){
        return clients.find(channelIdMap.get(id));
    }
    public static void send(Object msg){


//        Set<String> longId = channelIdMap.keySet();
//
//        Iterator<String> iterator = longId.iterator();
//        while(iterator.hasNext()){
//            String next = iterator.next();
//            ChannelId channelId = channelIdMap.get(next);
//
//            System.out.println(next);
//            System.out.println(channelId);
//        }

        //String request = ((TextWebSocketFrame) msg).text();

       // System.out.println(request);
        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
//
        String receiveStr = Hex.encodeHexString(bytes);
       // String receiveStr = ConvertCode.receiveHexToString(bytes);
      //  System.out.println("转换后的" + string);
       // String body = (String)msg;


        logger.info("原始报文-------：{}",receiveStr);
        //writeToClient(body,clients,"测试");
        writeToClient(receiveStr,clients,"测试");
        //new String(bytes), CharsetUtil.UTF_8
        //ByteBuf outBuffer = Unpooled.copiedBuffer(new String(bytes), CharsetUtil.UTF_8);
//        clients.writeAndFlush(byteBuf);
//        ReferenceCountUtil.release(byteBuf);
    }
    /**
     * 公用回写数据到客户端的方法
     * @param
     * @param group
     * @param mark 用于打印/log的输出
     * <br>//channel.writeAndFlush(msg);//不行
     * <br>//channel.writeAndFlush(receiveStr.getBytes());//不行
     * <br>在netty里，进出的都是ByteBuf，楼主应确定服务端是否有对应的编码器，将字符串转化为ByteBuf
     */
    private static void writeToClient(final String receiveStr, ChannelGroup group, final String mark) {
        try {
            ByteBuf bufff = Unpooled.buffer();//netty需要用ByteBuf传输
           // ConvertCode.hexString2Bytes(receiveStr)
           // Hex.decodeHex(receiveStr.toCharArray())
            bufff.writeBytes(Hex.decodeHex(receiveStr.toCharArray()));//对接需要16进制

           // group.writeAndFlush(bufff);
            group.forEach(channel -> {
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
                                    System.out.println(sb.toString()+"回写成功"+receiveStr);
                                    //log.info(sb.toString()+"回写成功"+receiveStr);
                                } else {
                                    System.out.println(sb.toString()+"回写失败"+receiveStr);
                                    // log.error(sb.toString()+"回写失败"+receiveStr);
                                }
                            }

                        });
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("调用通用writeToClient()异常"+e.getMessage());
            //log.error("调用通用writeToClient()异常：",e);
        }
    }

}
