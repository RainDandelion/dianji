package com.netty.Server;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.HashMap;
import java.util.Map;

public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端建立连接，通道开启");
        MyChannelHandlerPool.getInstance().add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道关闭，与客户端断开连接");
        MyChannelHandlerPool.getInstance().remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

        ByteBufAllocator alloc = channelHandlerContext.alloc();
        if (null != o && o instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest) o;
            String uri = request.uri();
            Map paramMap = getUrlParams(uri);
            System.out.println("接受到的参数：" + JSON.toJSONString(paramMap));
            if (uri.contains("?")){
                String newUri = uri.substring(0, uri.indexOf("?"));
                System.out.println(newUri);
                request.setUri(newUri);
            }
        }else if (o instanceof TextWebSocketFrame){
            //正常的TEXT消息类型
            TextWebSocketFrame frame=(TextWebSocketFrame)o;
            System.out.println("客户端收到服务器数据：" +frame.text());
            MyChannelHandlerPool.getInstance().writeAndFlush(new TextWebSocketFrame(frame.text()));
        }
    }

    private Map getUrlParams(String uri) {
        Map<String,String> uriMap = new HashMap<>();
        uri = uri.replace("?", ";");
        if (!uri.contains(";")){
            return uriMap;
        }
        if (uri.split(";").length > 0){
            String[] arr = uri.split(";")[1].split("&");
            for (String s:
                 arr) {
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                uriMap.put(key,value);

            }
            return uriMap;
        }else {
            return uriMap;
        }
    }
}
