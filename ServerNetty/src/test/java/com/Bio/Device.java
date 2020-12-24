package com.Bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Device {

    public static void main(String[] args) {
        try {

            Scanner in = new Scanner(System.in);
            String input = null;
            Socket socket = new Socket("localhost",5461);

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();




            while ((input = in.next()).length() > 0){

                outputStream.write(input.getBytes());

                //读
                byte[] bytes = new byte[1024];
                int length =0;
                while ((length = inputStream.read(bytes)) > 0){
                    System.out.println(new String(bytes,0,length));
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
