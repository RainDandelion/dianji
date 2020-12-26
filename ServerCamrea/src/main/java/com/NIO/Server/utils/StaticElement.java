package com.NIO.Server.utils;

public class StaticElement {
    public static final String ERROR="error";
    public static final String RESULT="result";
    public static final int leftDir=1;         //方向 左
    public static final int rightDir=0;        //方向 右


    public static final byte[] stopcmd=new byte[]{(byte) 0xf5,5, (byte) 0xff};  //停止指令   (byte) 0xf5,5, (byte) 0xff  -->  F5 05 FF  ？

    public static final String CAMERAIP="cameraIp";
    public static final String MOTOIP="motoIp";

    public static final int LEFT=0;
    public static final int RIGHT=1;




}
