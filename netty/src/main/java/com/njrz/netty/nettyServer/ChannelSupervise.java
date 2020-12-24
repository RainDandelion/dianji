package com.njrz.netty.nettyServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelSupervise {
    private static Logger logger = LoggerFactory.getLogger(ChannelSupervise.class);
    private  static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static Map<String, ChannelId> channelIdMap = new ConcurrentHashMap<>();
    public static void addChannel(Channel channel){
        /**
         * ShortText 短id可能会重复
         * asLongText()——唯一的ID
         */
        clients.add(channel);
        channelIdMap.put(channel.id().asShortText(),channel.id());
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

    /**
     * 发送数据（文本）
     * @param msg
     */
    public static void sendAll(ByteBuf msg){
        clients.writeAndFlush(msg);
    }
}
