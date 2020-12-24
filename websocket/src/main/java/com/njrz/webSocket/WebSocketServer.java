package com.njrz.webSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value ="/websocket/{sid}")
public class WebSocketServer {

    private static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private static int onlineCount = 0;

    private static CopyOnWriteArraySet<WebSocketServer> socketServers = new CopyOnWriteArraySet<>();



    private Session session;

    private String sid = "";

    @OnOpen
    public void Session(Session session, @PathParam("sid")String sid){
        this.session = session;
        socketServers.add(this);
        addOnlineCount();
        logger.info("有新窗口开始监听:"+sid+",当前在线人数为" + getOnlineCount());
        this.sid = sid;
        sendMessage("连接成功");
    }

    @OnClose
    public void onclose(){
        socketServers.remove(this);
        subOnlineCount();
        logger.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }


    @OnMessage
    public void OnMessage(String message,Session session){
        logger.info("收到来自窗口"+sid+"的信息:"+message);
        for (WebSocketServer socketServer:
             socketServers) {
            socketServer.sendMessage(message);
        }

    }
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误");
        error.printStackTrace();
    }

    private void sendMessage(String msg) {
        try {
            this.session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message) throws IOException {
        logger.info(message);
        for (WebSocketServer item :socketServers) {
            item.sendMessage(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }


}
