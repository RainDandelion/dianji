package com.Nio.Server1;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

public class BasicBuffer {

//    public static void main(String[] args) {
////        IntBuffer intBuffer = IntBuffer.allocate(5);
////
////
////        for (int i = 0; i < intBuffer.capacity(); i++) {
////            intBuffer.put(i);
////        }
////
////        intBuffer.flip();  //读写切换
////        intBuffer.position(1);
////        intBuffer.limit(3);
////        intBuffer.clear();
////        while (intBuffer.hasRemaining()){
////            System.out.println(intBuffer.get());
////        }
//
//        String s = "hello world";
//
//        try {
//            //FileInputStream fileInputStream = new FileInputStream("D:\\file01.txt");
//            RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\file01.txt","rw");
//            FileChannel channel = randomAccessFile.getChannel();
//
//
//            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//            int read = channel.read(byteBuffer);
//            if (read > 0){
//                byteBuffer.flip();
//                System.out.println(new String(byteBuffer.array()));
//            }
//
//
////            byteBuffer.put(s.getBytes());
////            byteBuffer.flip();
////
////            channel.write(byteBuffer);
////            randomAccessFile.close();
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\file01.txt","rw");

        FileChannel channel = randomAccessFile.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);


        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();

        while(true){
            byteBuffer.clear();
            int read = channel.read(byteBuffer);
            if(read == -1){
                System.out.println("读取成功");
                break;
            }
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
        }
    }

}
