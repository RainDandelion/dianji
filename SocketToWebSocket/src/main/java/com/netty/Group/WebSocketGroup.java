package com.netty.Group;

import io.netty.channel.Channel;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketGroup {
    private WebSocketGroup(){};


    private static WebSocketGroup instance = new WebSocketGroup();

    public static WebSocketGroup getInstance() {
        return instance;
    }
    /**
     * websocket
     * id ,channel
     */

    private  static Map<String, Channel> WebSocketMap = new ConcurrentHashMap<>();

    public void replaceId(Channel channel,String UserId){
        /**
         * 根据值 删除key-value
         */
        Collection<Channel> values = WebSocketMap.values();
        while (true == values.contains(channel)){
            values.remove(channel);
        }
        WebSocketMap.put(UserId,channel);
    }

    public void AddChannel(String longId,Channel channel){
        WebSocketMap.put(longId,channel);
    }

    public void removeChannel(Channel channel){
        /**
         * 根据值 删除key-value
         */
        Collection<Channel> values = WebSocketMap.values();
        while (true == values.contains(channel)){
            values.remove(channel);
        }
    }
    public int CountWebChannel(){
        return WebSocketMap.size();
    }





}
