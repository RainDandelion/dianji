package com.Stomp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller

public class controller {
    private static Logger logger = LoggerFactory.getLogger(controller.class);
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 该地址 /receive 接收前端发送过来的消息
     * 该地址 /topic/getResponse 是后端要将消息发送到的websocket客户端地址
     *
     * 参数的接收和传递按照springMVC的格式来就行了
     *
     * @param name
     * @return
     */
    @MessageMapping("/receive")
    @SendTo("/topic/getResponse")
    public String broadcast(@RequestParam String name) {
        logger.info("receive a message = {}", name);
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return "success: " + sdf.format(now);
    }


    public void send() {
        // 后台主动发送消息
        this.simpMessagingTemplate.convertAndSend("/topic/getResponse", "时间：" + new Date());
    }
}
