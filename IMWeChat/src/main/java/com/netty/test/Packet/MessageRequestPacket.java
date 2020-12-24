package com.netty.test.Packet;

import lombok.Data;

@Data
public class MessageRequestPacket extends Packet {


    private String Message;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
