package com.netty.Group;


import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketGroup {
    private SocketGroup(){};

    private static SocketGroup Instance = new SocketGroup();

    public static SocketGroup getInstance() {
        return Instance;
    }



    public static  Map<String, Channel> SocketMap = new ConcurrentHashMap<>();


    public void addChannel(String id,Channel channel){
        SocketMap.put(id, channel);
    }


    public void removeChannel(String id){
        SocketMap.remove(id);
    }

    public void sendMessage(byte[] bytes){
        SocketMap.forEach((s, channel) -> {
            channel.writeAndFlush(Unpooled.copiedBuffer(bytes));
        });
    }
}
