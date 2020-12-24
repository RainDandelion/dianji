package com.netty.client;


import com.netty.proto.SubscribeReqProto;
import com.netty.proto.SubscribeRespProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;


public class SubReqProHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i=0;i<10;i++){
            ctx.write(subReq(i));
        }
        ctx.flush();
    }
    private SubscribeReqProto.SubscribeReq subReq(int i){
        SubscribeReqProto.SubscribeReq.Builder req=SubscribeReqProto.SubscribeReq.newBuilder();
        req.setProductName("Netty Book");
        req.setUserName("啥啥啥");
        req.setSubReqId(i);
        List<String> address=new ArrayList<String>();
        address.add("aaaaaaaaaaaaaaaaaaa");
        address.add("bbbbbbbbbbbbbbbbbbb");
        address.add("ccccccccccccccccccc");
        req.addAllAddress(address);
        return req.build();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接收服务器消息是:"+msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}