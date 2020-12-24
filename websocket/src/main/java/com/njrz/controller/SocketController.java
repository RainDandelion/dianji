package com.njrz.controller;


import com.njrz.webSocket.WebSocketServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class SocketController {


    @Resource
    private WebSocketServer socketServer;
    @GetMapping("/test")
    public void send() throws IOException {
        WebSocketServer.sendInfo("f5 05 02 00 00 09 ff ff ff ff ff ff 12");
    }

}
