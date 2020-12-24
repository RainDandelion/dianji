package com.netty.Server;

import com.netty.WebSocket.WebSocketChannelInitializer;
import com.netty.socket.ServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NettyServer {


    private Logger logger = LoggerFactory.getLogger(NettyServer.class);
    //两个事件循环组 boss获取连接发送 worker接收处理
    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup worker = new NioEventLoopGroup();
    //server启动器
    ServerBootstrap serverSocketBootstrap = new ServerBootstrap();
    ServerBootstrap serverWebSocketBootstrap = new ServerBootstrap();

    @Value("${netty.SocketPort}")
    private Integer SocketPort;
    @Value("${netty.WebSocketPort}")
    private Integer WebSocketPort;
  public void bind(){
      logger.info("服务器正在启动");
          serverWebSocketBootstrap.group(boss, worker).
                  channel(NioServerSocketChannel.class)
                  .childHandler(new WebSocketChannelInitializer());
          serverSocketBootstrap.group(boss, worker)
                  .channel(NioServerSocketChannel.class)
                  .childHandler(new ServerInitializer());

      try {
          ChannelFuture WebSocket = serverWebSocketBootstrap.bind(WebSocketPort).sync();///websocket
          logger.info("网页监控服务端:{}完成启动",WebSocket.channel().localAddress());

          ChannelFuture Socket = serverSocketBootstrap.bind(SocketPort).sync();  //socket
          logger.info("电机监控服务端:{}完成启动",Socket.channel().localAddress());
          WebSocket.channel().closeFuture().sync();
          Socket.channel().closeFuture().sync();
      } catch (InterruptedException e) {
          e.printStackTrace();
      }finally {
          boss.shutdownGracefully();
          worker.shutdownGracefully();
      }



  }


}
