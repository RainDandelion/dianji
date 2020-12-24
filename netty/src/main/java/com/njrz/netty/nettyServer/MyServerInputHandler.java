package com.njrz.netty.nettyServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Date;

import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;

//SimpleChannelInboundHandler<Object>
public class MyServerInputHandler extends ChannelInboundHandlerAdapter{


    private static Logger logger = LoggerFactory.getLogger(MyServerInputHandler.class);
    private WebSocketServerHandshaker handshaker;
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    /**
     * 活跃
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        SocketChannel socketChannel = (SocketChannel)ctx.channel();

        logger.info("连接报告开始");
        logger.info("链接报告信息：有一客户端链接到本服务端");
        logger.info("链接报告IP:{}", socketChannel.localAddress().getHostName());
        logger.info("链接报告Port:{}", socketChannel.localAddress().getPort());
        logger.info("链接报告完毕");
//        String str = "通知客户端链接建立成功" + " " + new Date() + " " + socketChannel.localAddress().getHostString() + "\r\n";
        ChannelSupervise.addChannel(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    /**
     * 心跳
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("异常信息：\r\n" + cause.getMessage());

        ctx.close();
        ctx.channel().close();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("收到消息："+msg);
        if (msg instanceof FullHttpRequest){
//            //以http请求形式接入，但是走的是websocket
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }else if (msg instanceof WebSocketFrame){
//            //处理websocket客户端的消息
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        logger.info("收到消息："+msg);
//        if (msg instanceof FullHttpRequest){
//            //以http请求形式接入，但是走的是websocket
//            handleHttpRequest(ctx, (FullHttpRequest) msg);
//        }else if (msg instanceof WebSocketFrame){
//            //处理websocket客户端的消息
//            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
//        }
//
//    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) {
        if (msg instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(),((CloseWebSocketFrame) msg).retain());
            return;
        }
        if (msg instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
            return;
        }
//        if (!(msg instanceof TextWebSocketFrame)){
//            logger.info("仅支持文本消息，不支持二进制消息");
//            throw new UnsupportedOperationException(String.format(
//                    "%s frame types not supported", msg.getClass().getName()));
//        }
        //返回应答消息
//        String request = ((TextWebSocketFrame) msg).text();
//
//
//        logger.info("服务端收到 ： {}",request);
//        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(new Date().toString()
//                + ctx.channel().id().asLongText() + "：" + request);

       // ByteBuf content = ((BinaryWebSocketFrame) msg).content();
        ByteBuf content = msg.content();
        byte[] bytes = new byte[content.readableBytes()];
        content.readBytes(bytes);
        content.retain();
        logger.info("服务端收到 ： {}",new String(bytes));


        ChannelSupervise.sendAll(content);
    }

    /**
     * 唯一的一次http请求，用于创建websocket
     * @param ctx
     * @param msg
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {
        //要求Upgrade为websocket，过滤掉get/Post
        if (!msg.decoderResult().isSuccess()
                || (!"websocket".equals(msg.headers().get("Upgrade")))){
            //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
            sendHttpResponse(ctx,msg,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:22200/ws", null, false);
        handshaker = wsFactory.newHandshaker(msg);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), msg);
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest msg, DefaultFullHttpResponse defaultFullHttpResponse) {
        //返回应答给客户端
        if (defaultFullHttpResponse.status().code() !=200){
            ByteBuf byteBuf = Unpooled.copiedBuffer(defaultFullHttpResponse.status().toString(), CharsetUtil.UTF_8);
            defaultFullHttpResponse.content().writeBytes(byteBuf);
            byteBuf.release();
        }
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(defaultFullHttpResponse);

        // 如果是非Keep-Alive，关闭连接
        if (!isKeepAlive(msg) || defaultFullHttpResponse.status().code() != 200) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断开连接
        logger.debug("客户端断开连接："+ctx.channel());
        ChannelSupervise.removeChannel(ctx.channel());
    }

//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        clients.add(ctx.channel());
//        logger.info("客户端连接  ——   channel id : {}", ctx.channel().id().asLongText());
//        logger.info("剩余客户端：{}", clients.size());
//        clients.writeAndFlush(new TextWebSocketFrame("连接成功！当前在线人数：" + clients.size()));
//        super.handlerAdded(ctx);
//    }
//
//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//        logger.info("客户端连接断开  ——   channel id : {}", ctx.channel().id().asLongText());
//        logger.info("剩余客户端：{}", clients.size());
//        super.handlerRemoved(ctx);
//    }
}
