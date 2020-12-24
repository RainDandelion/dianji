package com.netty.test.Packet;

import lombok.Data;

/**
 * 登录请求包
 */
@Data
public class LoginRequestPacket extends Packet{
    private String userId;

    private String userName;

    private String Password;



    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
