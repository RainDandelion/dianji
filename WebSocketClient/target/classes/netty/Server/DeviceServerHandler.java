package netty.Server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ChannelHandler.Sharable
public class DeviceServerHandler extends SimpleChannelInboundHandler<Object> {
    private Map<String, Channel> map = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(DeviceServerHandler.class);

    //由于继承了SimpleChannelInboundHandler，这个方法必须实现，否则报错
    //但实际应用中，这个方法没被调用
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buff = (ByteBuf) msg;
        String info = buff.toString(CharsetUtil.UTF_8);
        logger.info("收到消息内容："+info);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        // WebSocket消息处理
        if (msg instanceof WebSocketFrame) {
            logger.info("WebSocket消息处理************************************************************");
            String webSocketInfo = ((TextWebSocketFrame) msg).text().trim();
            logger.info("收到webSocket消息：" + webSocketInfo);
        }
        // Socket消息处理
        else{
            logger.info("Socket消息处理=================================");
            ByteBuf buff = (ByteBuf) msg;
            String socketInfo = buff.toString(CharsetUtil.UTF_8).trim();;
            logger.info("收到socket消息："+socketInfo);
        }
    }

    /*******************************************************************************************/
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                //socketChannelMap.remove((SocketChannel)ctx.channel());
                ctx.disconnect();
                logger.info("心跳检测触发，socket连接断开！");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress reAddr = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIP = reAddr.getAddress().getHostAddress();
        String clientPort = String.valueOf(reAddr.getPort());
        logger.info("连接断开："+ clientIP +":"+ clientPort);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.name());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }
}