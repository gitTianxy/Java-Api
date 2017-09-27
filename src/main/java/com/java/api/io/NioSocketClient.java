package com.java.api.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * Created by kevintian on 2017/9/27.
 */
public class NioSocketClient implements Runnable {
    @Override
    public void run() {
        try {
            write();
//            read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void read() throws IOException {
        SocketChannel readChanel = SocketChannel.open();
        readChanel.connect(new InetSocketAddress("localhost", 1111));
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        int byteRead = readChanel.read(buffer);
        while (byteRead != -1) {
            buffer.flip();
            String serverMsg = new String(buffer.array(), 0, byteRead, "utf-8");
            System.out.println("***client receive:"+serverMsg);
            buffer.clear();
            if (serverMsg.contains("FINISH")) {
                break;
            }
            byteRead = readChanel.read(buffer);
        }
        readChanel.close();
    }

    void write() throws IOException, InterruptedException {
        InetSocketAddress crunchifyAddr = new InetSocketAddress("localhost", 1111);
        SocketChannel crunchifyClient = SocketChannel.open(crunchifyAddr);
        System.out.println("Connecting to Server on port 1111...");
        ArrayList<String> companyDetails = new ArrayList<String>();
        companyDetails.add("Facebook");
        companyDetails.add("Twitter");
        companyDetails.add("IBM");
        companyDetails.add("Google");
        companyDetails.add("FINISH");
        for (String companyName : companyDetails) {
            System.out.println("***client sending: " + companyName);
            byte[] message = companyName.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(message);
            while (buffer.hasRemaining()) {
                crunchifyClient.write(buffer);
            }
            buffer.clear();
            Thread.sleep(2000);
        }
        crunchifyClient.close();
    }
}
