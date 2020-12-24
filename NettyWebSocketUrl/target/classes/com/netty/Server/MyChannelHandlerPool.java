package com.netty.Server;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyChannelHandlerPool {

    private MyChannelHandlerPool(){

    };
    private static ChannelGroup Instance = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ChannelGroup getInstance() {
        return Instance;
    }
}
