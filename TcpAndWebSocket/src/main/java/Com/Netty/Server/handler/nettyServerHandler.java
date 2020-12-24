package Com.Netty.Server.handler;


import Com.Netty.Server.Group.GlobalChannelGroup;
import Com.Netty.Server.utils.WriteToSocketClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 连接电机
 */
public class nettyServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(nettyServerHandler.class);
   private ChannelGroup GlobalChannelGroupInstance = GlobalChannelGroup.getINSTANCE();


 

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        logger.info("电机客户端 ：{},加入",ctx.channel().remoteAddress());
        GlobalChannelGroupInstance.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;
//        byte[] bytes = new byte[byteBuf.readableBytes()];
//        byteBuf.readBytes(bytes);
//
//        System.out.println(new String(bytes, CharsetUtil.UTF_8));


        String temp = (String) msg;
        logger.info("原始报文-------：{}",temp);
        WriteToSocketClient.writeToClient(temp,GlobalChannelGroupInstance,"测试");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("电机客户端：{}",ctx.channel().remoteAddress());
        GlobalChannelGroupInstance.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
        ctx.close();
    }
}
