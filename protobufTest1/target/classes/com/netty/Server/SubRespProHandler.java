package com.netty.Server;



import com.netty.proto.SubscribeReqProto;
import com.netty.proto.SubscribeRespProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class SubRespProHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqProto.SubscribeReq req=(SubscribeReqProto.SubscribeReq)msg;
        System.out.println("服务器接收客户端消息是:"+req.toString());
        ctx.writeAndFlush(resp(req.getSubReqId()));
    }

    private SubscribeRespProto.SubscribeResp resp(int subReqId)throws Exception{
        SubscribeRespProto.SubscribeResp.Builder resp= SubscribeRespProto.SubscribeResp.newBuilder();
        resp.setSubRepId(subReqId);
        resp.setResCode(0);
        resp.setDesc("aaa,网上预定成功3天后，发送到指定的地址");
        return resp.build();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
