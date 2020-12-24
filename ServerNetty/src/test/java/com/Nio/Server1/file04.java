package com.Nio.Server1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;

public class file04 {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\2020-12-10_164859.png","rw");
        RandomAccessFile fileOutputStream = new RandomAccessFile("D:\\2020-12-10_1648523219.png","rw");

        FileChannel channel1 = randomAccessFile.getChannel();
        FileChannel channel = fileOutputStream.getChannel();

        channel.transferFrom(channel1,0,channel1.size());

    }
}
