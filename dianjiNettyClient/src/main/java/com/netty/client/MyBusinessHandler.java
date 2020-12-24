package com.netty.client;

import com.netty.Server.IBusinessServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

public class MyBusinessHandler extends ChannelInboundHandlerAdapter {


    @Autowired
    private IBusinessServer businessServer;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String str = (String)msg;
        if (ctx.executor().inEventLoop()){
            testBusExec(ctx,str);
        }else {
            ctx.executor().execute(()->{
                testBusExec(ctx,str);
            });
        }

        super.channelRead(ctx, msg);
    }

    private void testBusExec(ChannelHandlerContext ctx, String str) {
            businessServer.testBus();
    }
}
