package Com.Netty.Server.handler;


import Com.Netty.Server.Group.GlobalChannelGroup;
import Com.Netty.Server.Group.GlobalWebChannelGroup;
import Com.Netty.Server.utils.WriteToSocketClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class nettyWebServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private Logger logger = LoggerFactory.getLogger(nettyWebServerHandler.class);
    private ChannelGroup GlobalWebChannelGroupInstance = GlobalWebChannelGroup.getINSTANCE();  //websocket客户端
    private ChannelGroup GlobalChannelGroupInstance = GlobalChannelGroup.getINSTANCE();  //电机客户端
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String message = msg.text().replace(" ","");
        logger.info("当前连接数: {}",GlobalWebChannelGroupInstance.size());
        logger.info("原始报文-------：{}",message);

        GlobalWebChannelGroupInstance.forEach(o->{
                //socket
//            String string = ConvertCode.string2HexString(message);
//            System.out.println(string);
            WriteToSocketClient.writeToClient(message,GlobalChannelGroupInstance,"测试");
                //websocket
            TextWebSocketFrame text1 = new TextWebSocketFrame(o.remoteAddress() + "发送消息：" + message + "\n");
            o.writeAndFlush(text1);

        });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel ch = ctx.channel();
        GlobalWebChannelGroupInstance.add(ch);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info(ctx.channel().remoteAddress()+":离开");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel ch = ctx.channel();
        logger.info(ch.remoteAddress()+"：连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
        ctx.close();
    }


}
