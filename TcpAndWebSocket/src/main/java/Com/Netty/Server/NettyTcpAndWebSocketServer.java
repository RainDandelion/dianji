package Com.Netty.Server;

import Com.Netty.Server.code.HexDecode;
import Com.Netty.Server.handler.SocketChooseHandler;
import Com.Netty.Server.handler.nettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
public class NettyTcpAndWebSocketServer {
    @Value("${netty.port}")
    private  Integer PORT;

    private EventLoopGroup Boss = new NioEventLoopGroup();
    private EventLoopGroup Work = new NioEventLoopGroup();
    private Logger logger = LoggerFactory.getLogger(NettyTcpAndWebSocketServer.class);



    public void init(){

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(Boss,Work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true) //快速复用端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * 初始化选择
                         * @param socketChannel
                         * @throws Exception
                         */
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast( new SocketChooseHandler());

                            //            ChannelPipeline channelPipeline = channelHandlerContext.pipeline();
                            pipeline.addLast(new HexDecode());
////            // channelPipeline.addLast(new StringDecoder());
                            pipeline.addLast(new nettyServerHandler());
                            pipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));  //心跳检测
                        }
                    });
            ChannelFuture Server = serverBootstrap.bind(PORT).sync();
            logger.info("服务端:{}完成启动",Server.channel().localAddress());
            Server.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            Boss.shutdownGracefully();
            Work.shutdownGracefully();
        }

    }
    @PreDestroy
    public void Destroy(){
        try {
            Boss.shutdownGracefully().sync();
            Work.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
