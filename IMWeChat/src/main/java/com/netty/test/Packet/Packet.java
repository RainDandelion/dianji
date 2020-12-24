package com.netty.test.Packet;

import lombok.Data;

@Data
public abstract class Packet {
    private Byte Version =1 ;

    public abstract Byte getCommand();
}
