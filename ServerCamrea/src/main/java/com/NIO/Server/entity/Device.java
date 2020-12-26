package com.NIO.Server.entity;

import lombok.Data;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
@Data

public class Device {

    private boolean connectState = false;

    private String motoIp;

    private String cameraIP;



    private Socket socket;



    private SocketChannel socketChannel;

    private ByteBuffer byteBuffer;

    private byte errorFlag;

}
