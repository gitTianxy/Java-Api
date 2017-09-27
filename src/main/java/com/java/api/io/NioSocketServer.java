package com.java.api.io;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by kevintian on 2017/9/27.
 */
public class NioSocketServer implements Runnable {
    @Override
    public void run() {
        try {
            // Selector: multiplexor of SelectableChannel objects
            Selector selector = Selector.open(); // selector is open here

            // ServerSocketChannel: selectable channel for stream-oriented listening sockets
            ServerSocketChannel crunchifySocket = ServerSocketChannel.open();
            InetSocketAddress crunchifyAddr = new InetSocketAddress("localhost", 1111);

            // Binds the channel's socket to a local address and configures the socket to listen for connections
            crunchifySocket.bind(crunchifyAddr);

            // Adjusts this channel's blocking mode.
            crunchifySocket.configureBlocking(false);

            int ops = crunchifySocket.validOps();
            SelectionKey selectKy = crunchifySocket.register(selector, ops, null);

            // Infinite loop..
            // Keep server running
            while (true) {

                System.out.println("i'm a server and i'm waiting for new connection and buffer select...");
                // Selects a set of keys whose corresponding channels are ready for I/O operations
                selector.select();

                // token representing the registration of a SelectableChannel with a Selector
                Set<SelectionKey> crunchifyKeys = selector.selectedKeys();
                Iterator<SelectionKey> crunchifyIterator = crunchifyKeys.iterator();

                while (crunchifyIterator.hasNext()) {
                    SelectionKey myKey = crunchifyIterator.next();

                    if (myKey.isAcceptable()) {
                        SocketChannel crunchifyClient = crunchifySocket.accept();

                        crunchifyClient.configureBlocking(false);

                        // Operation-set bit for read operations
                        crunchifyClient.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        System.out.println("Connection Accepted: " + crunchifyClient.getLocalAddress() + "\n");

                    } else if (myKey.isReadable()) {
                        SocketChannel crunchifyClient = (SocketChannel) myKey.channel();
                        ByteBuffer crunchifyBuffer = ByteBuffer.allocate(256);
                        crunchifyClient.read(crunchifyBuffer);
                        String result = new String(crunchifyBuffer.array()).trim();

                        System.out.println("Message received: " + result);

                        if (result.equals("FINISH")) {
                            crunchifyClient.write(ByteBuffer.wrap("FINISH".getBytes()));
                            crunchifyClient.close();
                            System.out.println("\nIt's time to close connection as we got FINISH order");
                            System.out.println("\nServer will keep running and waiting for request from client");
                        }
                    } else if (myKey.isWritable()) {
                        SocketChannel client = (SocketChannel) myKey.channel();
                        String serverMsg = "MESSAGE FROM SOCKET SERVER";
                        ByteBuffer writeBuf = ByteBuffer.wrap(serverMsg.getBytes());
                        System.out.println("Server sending message to client");
                        while (writeBuf.hasRemaining()) {
                            client.write(writeBuf);
                        }
                        Thread.sleep(1000);
                    }
                    crunchifyIterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
