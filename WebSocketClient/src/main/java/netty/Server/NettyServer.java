package netty.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Component
public class NettyServer {
    private static Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Resource
    private ChannelActiveHandler channelActiveHandler;
    @Resource
    private DeviceServerHandler deviceServerHandler;

    //配置服务端线程组
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workGroup = new NioEventLoopGroup();
    ChannelFuture socketfuture = null;

    @PreDestroy             //关闭spring容器后释放资源
    public void stop(){
        if(socketfuture!=null){
            socketfuture.channel().close().addListener(ChannelFutureListener.CLOSE);
            socketfuture.awaitUninterruptibly();
            socketfuture=null;
            logger.info("Netty 服务端关闭");
        }

        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    /**
     * 启动流程
     */
    public void run(int port) throws InterruptedException {

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_REUSEADDR, true) //快速复用端口
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS,1000)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast("active",channelActiveHandler);
                            //Socket 连接心跳检测
                            ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(60, 0, 0));

                            ch.pipeline().addLast("socketChoose",new SocketChooseHandler());

                            //注意，这个专门针对 Socket 信息的解码器只能放在 SocketChooseHandler 之后，否则会导致 webSocket 连接出错
                            // ch.pipeline().addLast("myDecoder",new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN,1024*1024,0,4,0,4,true));
                            ch.pipeline().addLast(new HexDecode());
                            ch.pipeline().addLast("commonhandler",deviceServerHandler);
                        }
                    });

            //绑定端口，同步等待成功
            socketfuture = serverBootstrap.bind(port).sync();
            if(socketfuture.isSuccess()){
                logger.info("Netty 服务已启动");
            }

            socketfuture.channel().closeFuture().sync();

        }catch (Exception e){
            //优雅退出，释放线程池
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }finally {
            //优雅退出，释放线程池
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
