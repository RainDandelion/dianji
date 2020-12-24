package com.njrz.Netty;

import com.google.gson.Gson;
import com.sun.deploy.util.URLUtil;
import com.sun.jndi.toolkit.url.UrlUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ChannelHandler.Sharable
public class MyServerHandler  extends SimpleChannelInboundHandler<Object> {
    private Logger logger = LoggerFactory.getLogger(MyServerHandler.class);

    private WebSocketServerHandshaker handshaker;


    private int lossConnectCount = 0;  //记录次数
    /**
     ////     * 记录和管理所有客户端
     ////     */
  private  static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
        SocketChannel channel = (SocketChannel) ctx.channel();
        logger.info("链接报告开始");
        logger.info("链接报告信息：有一客户端链接到本服务端");
        logger.info("链接报告IP:{}", channel.localAddress().getHostString());
        logger.info("链接报告Port:{}", channel.localAddress().getPort());
        logger.info("链接报告完毕");
        //通知客户端链接建立成功
        String str = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
        ctx.writeAndFlush(str);
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        logger.info("异常信息：\r\n" + cause.getMessage());
        ctx.close();
        ctx.channel().close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //clients.add(ctx.channel());
        logger.info("客户端连接  ——   channel id : {}", ctx.channel().id().asLongText());
        logger.info("剩余客户端：{}", clients.size());
            //ctx.channel().writeAndFlush(new TextWebSocketFrame("连接成功！当前在线人数：" + clients.size()));
        clients.writeAndFlush(new TextWebSocketFrame("连接成功！当前在线人数：" + clients.size()));
        clients.add(ctx.channel());
    }
        @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //触发该方法ChannelGroup会自动移除对应客户端的channel，所以不需要专门移除
//            clients.remove(ctx.channel());
        logger.info("客户端连接断开  ——   channel id : {}", ctx.channel().id().asLongText());
        logger.info("剩余客户端：{}", clients.size());
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        lossConnectCount = 0;
//        TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) o;
//        logger.info("接受的信息为{}",textWebSocketFrame.text());
//        String date = new Date().toString();
//
//        channelHandlerContext.writeAndFlush(new TextWebSocketFrame("现在时刻:"+date+"发送了:"+textWebSocketFrame.text()));


        clients.forEach(channel -> {
            //在每一个channel中判断
            //传统的http接入
            if(o instanceof FullHttpRequest){
                System.out.println("into hettpHandle");
                handleHttpRequest(channelHandlerContext,(FullHttpRequest) o);
            }
            //webSocket接入
            else if(o instanceof WebSocketFrame){
                System.out.println("into websockethandel");
                handleWebsocketFrame(channelHandlerContext,(WebSocketFrame) o);
            }
        });
    }
    /**
     * websocket
     * @param ctx
     * @param frame
     */
    private void handleWebsocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if(frame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(),(CloseWebSocketFrame) frame.retain());
            return;
        }
        if(frame instanceof PingWebSocketFrame){
            ctx.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }



        String req = ((TextWebSocketFrame)frame).text();
        logger.info("接受的信息为{}",req);
        String date = new Date().toString();

        clients.writeAndFlush(new TextWebSocketFrame("现在时刻:"+date+"发送了:"+req));
    }

    /**
     * http
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {

//        Gson gson = new Gson();
//        String uri = req.uri();
//        Map paramMap = getUrlParams(uri);
//        System.out.println("接收到的参数是："+ gson.toJson(paramMap));
//        //如果url包含参数，需要处理
//        if(uri.contains("?")){
//            String newUri=uri.substring(0,uri.indexOf("?"));
//            System.out.println(newUri);
//            req.setUri(newUri);
//        }
//        String date = new Date().toString();
//        ctx.writeAndFlush(new TextWebSocketFrame("现在时刻:"+date));



//        ByteBuf content = req.content();
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(content.toString(CharsetUtil.UTF_8));
//        String pa = stringBuilder.toString();
//        logger.info("接受的参数为{}",pa);
//        String date = new Date().toString();
//
//        ctx.writeAndFlush(new TextWebSocketFrame("现在时刻:"+date+"发送了:"+pa));


//        System.out.println("uri:"+req.uri());
//        HttpMethod method = req.method();
//        System.out.println("当前请求的方式："+method.name());
//        ByteBuf in = req.content();
//        byte[] bs = in.array();
//        try {
//            System.out.println(new String(bs,"UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        //这里使用发送json数据返回
//        String msg="{\"result\":\"success\",\"uri\":\""+req.uri()+"\"}";
//        FullHttpResponse httpResponse=new DefaultFullHttpResponse(
//                HttpVersion.HTTP_1_1,
//                HttpResponseStatus.OK,
//                Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
//        httpResponse.headers()
//                .set(HttpHeaderNames.CONNECTION,"Keep-Alive")
//                /* .set(HttpHeaderNames.CONTENT_ENCODING,"gzip") */
//                .set(HttpHeaderNames.CONTENT_TYPE, "text/json; charset=UTF-8")
//                .set(HttpHeaderNames.DATE,new Date())
//                .set(HttpHeaderNames.SERVER,"BWS/1.1")
//                .set(HttpHeaderNames.SET_COOKIE,"delPer=0; path=/; domain=.baidu.com");
//        //发送请求
//        ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);


        //构造握手响应返回
//        WebSocketServerHandshakerFactory webSocketServerHandshakerFactory=new WebSocketServerHandshakerFactory("ws://localhost:7397/ws",null,false);
//        handshaker=webSocketServerHandshakerFactory.newHandshaker(req);
//        handshaker.handshake(ctx,req);


        logger.info("httprequest get");
        //Map<String,String> parmMap= new HashMap<>();
        if(req instanceof HttpRequest){
            HttpMethod method = req.method();
            logger.info("this is httpconnect");
            if ("/ws".equalsIgnoreCase(req.uri())){
                logger.info("websocket 请求介入");
                WebSocketServerHandshakerFactory webSocketServerHandshakerFactory=
                        new WebSocketServerHandshakerFactory("ws://localhost:7397/ws",null,false);
                handshaker=webSocketServerHandshakerFactory.newHandshaker(req);
                if(handshaker ==null){
                    WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                }else {
                    handshaker.handshake(ctx.channel(), req);
                }
            }
            if(HttpMethod.POST == method){
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
                decoder.offer(req);
                System.out.println(decoder.getBodyHttpDatas());
            }
            if (HttpMethod.GET == method) {
                // 是GET请求
                System.out.println(req.content());
                // 编码解码
                ByteBuf in = (ByteBuf) req.content();
                byte[] byt = new byte[in.readableBytes()];
                in.readBytes(byt);
                String body = null;
                try {
                    body = new String(byt, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                logger.info("server channelRead...; received收到客户端消息:{}",body);
                QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
                System.out.println(decoder.toString());
                /*
                 * ctx.channel().writeAndFlush(new
                 * TextWebSocketFrame("服务端数据"+body));
                 */
                // 将数据写入通道
                ctx.channel().writeAndFlush(new TextWebSocketFrame(body));
            }
        }

    }
    // 握手请求不成功时返回的应答
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
    }
    // 处理Websocket的代码
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否是关闭链路的指令
        System.out.println("websocket get");
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 文本消息，不支持二进制消息
        if (frame instanceof TextWebSocketFrame) {
            // 返回应答消息
            String request = ((TextWebSocketFrame) frame).text();
            ctx.channel().writeAndFlush(new TextWebSocketFrame(
                    request + " , 欢迎使用Netty WebSocket服务，现在时刻：" + new java.util.Date().toString()));
        }
    }


    private static Map getUrlParams(String url){
        Map<String,String> map = new HashMap<>();
        url = url.replace("?",";");
        if (!url.contains(";")){
            return map;
        }
        if (url.split(";").length > 0){
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr){
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key,value);
            }
            return  map;

        }else{
            return map;
        }
    }

    /**
     * 服务端心跳检测
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {


        logger.info("已经60秒没有收到客户端的消息了");

        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state() == IdleState.READER_IDLE){
                lossConnectCount++;
//                if(lossConnectCount > 2){
//                    logger.info("关闭不活跃通道");
//                    ctx.channel().close();
//                }
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }

//    @Override
//    protected void messageReceived(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
//        System.out.println("客户端收到服务器数据:" + msg.text());
//        Scanner s = new Scanner(System.in);
//        System.out.println("服务器推送：");
//        while(true) {
//            String line = s.nextLine();
//            if(line.equals("exit")) {
//                ctx.channel().close();
//                break;
//            }
//            String resp= "(" +ctx.channel().remoteAddress() + ") ：" + line;
//            ctx.writeAndFlush(new TextWebSocketFrame(resp));
//        }
//    }

//    private Logger logger = LoggerFactory.getLogger(MyServerHandler.class);
////    /**
////     * 记录和管理所有客户端
////     */
////    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
////
////    @Override
////    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
////        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 服务端接收到消息：" + msg);
////
////        for (Channel channel : clients) {
////            channel.writeAndFlush(new TextWebSocketFrame("服务器消息[" + ctx.channel().id().asLongText() + "]: " + msg));
////        }
////
//////            clients.writeAndFlush(new TextWebSocketFrame("服务器消息[" + ctx.channel().id().asLongText() + "]: " + content));
////    }
////
////
//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        clients.add(ctx.channel());
//        log.info("客户端连接  ——   channel id : {}", ctx.channel().id().asLongText());
//        log.info("剩余客户端：{}", clients.size());
////            ctx.channel().writeAndFlush(new TextWebSocketFrame("连接成功！当前在线人数：" + clients.size()));
//    }
////
////    @Override
////    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
////        //触发该方法ChannelGroup会自动移除对应客户端的channel，所以不需要专门移除
//////            clients.remove(ctx.channel());
////        log.info("客户端连接断开  ——   channel id : {}", ctx.channel().id().asLongText());
////        log.info("剩余客户端：{}", clients.size());
////    }
////
////    @Override
////    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
////        cause.printStackTrace();
////        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup中移除
////        ctx.channel().close();
////        clients.remove(ctx.channel());
////    }
//
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        SocketChannel channel = (SocketChannel) ctx.channel();
//        logger.info("链接报告开始");
//        logger.info("链接报告信息：有一客户端链接到本服务端");
//        logger.info("链接报告IP:{}", channel.localAddress().getHostString());
//        logger.info("链接报告Port:{}", channel.localAddress().getPort());
//        logger.info("链接报告完毕");
//
//        //通知客户端链接建立成功
//        String str = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
//        ctx.writeAndFlush(str);
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        logger.info("客户端断开链接{}", ctx.channel().localAddress().toString());
//    }
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//
//        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 服务端接收到消息：" + msg);
//        //通知客户端链消息发送成功
//        String str = "服务端收到：" + new Date() + " " + msg + "\r\n";
//        ctx.writeAndFlush(str);
////        ByteBuf byteBuf=(ByteBuf)msg;
////        byte[] bytes=new byte[byteBuf.readableBytes()];
////        byteBuf.readBytes(bytes);
////        System.out.println("Server Accept:"+new String(bytes, CharsetUtil.UTF_8));
////
////        ByteBuf outBuffer = Unpooled.copiedBuffer("hi netty client", CharsetUtil.UTF_8);
////        ctx.writeAndFlush(outBuffer);
//    }
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//
//        logger.info("异常信息：\r\n" + cause.getMessage());
//        ctx.close();
//        ctx.channel().close();
//    }
}
