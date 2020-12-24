package com.netty.Group;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * socket
 */
public class GlobalChannelGroup {
    private GlobalChannelGroup(){

    }

    public static ChannelGroup INSTANCE = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    public static ChannelGroup getINSTANCE() {
        return INSTANCE;
    }



}
