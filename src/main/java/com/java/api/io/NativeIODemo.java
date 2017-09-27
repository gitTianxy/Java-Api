package com.java.api.io;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by kevintian on 2017/9/27.
 */
public class NativeIODemo {
    final String TEST_ROOT = "C:/Users/kevintian/Desktop/io_test/";

    public static void main(String[] args) throws Exception {
        NativeIODemo demo = new NativeIODemo();
        demo.ioApi();
        demo.nioApi();
        demo.socketIo();
    }

    /**
     * CORE MEMBERS:
     * 1. Buffers: ByteBuffer, CharBuffer, other data-buffers
     * 2. Channels: FileChannel, SocketChannel/ServerSocketChannel, DatagramChannel
     * 3. Selectors: [relations] one selector with many channels, one channel with one or more buffer
     * <p>
     * UTILITIES:
     * 1. Files
     * 2. Paths
     */
    void nioApi() throws Exception {
        channelDemo();
        bufferDemo();
        selectorDemo();
    }

    /**
     * METHODS:
     * 1. read(buffer): write data into buffer(s)
     *      当写入buffer数组时, 按数组元素顺序依次写入, 一个写满写下一个
     * 2. write(buffer): read data from buffer(s)
     *      当从buffer数组读取时, 按数组元素顺序依次读入, 一个读完读下一个
     * 3. close(): close channel after use
     * UTILITIES: Channels
     * @throws IOException
     */
    private void channelDemo() throws IOException, InterruptedException {
        fileChannelDemo();
        socketChannelDemo();
    }

    /**
     *
     * @throws IOException
     */
    private void socketChannelDemo() throws IOException, InterruptedException {
        System.out.println(StringUtils.center("socketChannelDemo", 100, "="));
        Executor exec = Executors.newFixedThreadPool(2);
        exec.execute(new NioSocketServer());
        exec.execute(new NioSocketClient());
    }

    /**
     * FileChannel
     *      1) transferFrom(): read data from other channel
     *      2) transferTo(): write data to other channel
     *      3) postion(): operate position in the related file
     *      4) size(): size of the related file
     * CONSTRUCTION: from InputStream, OutputStream, RandomAccessFile
     * @throws IOException
     */
    private void fileChannelDemo() throws IOException {
        // read
        RandomAccessFile aFile = new RandomAccessFile(TEST_ROOT + "elk学习测试.md", "r");
        FileChannel inChannel = aFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(64);
        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {
            System.out.println(StringUtils.center("Read " + bytesRead + " bytes", 50, "-"));
            buf.flip();
            System.out.println(new String(buf.array(), 0, bytesRead, "utf-8"));
            /* 适合英文
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            */
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
        inChannel.close();

        // write
        File output = new File(TEST_ROOT + "out.md");
        if (!output.exists()) {
            Files.createFile(output.toPath());
        }
        FileChannel outChannel = FileChannel.open(Paths.get(TEST_ROOT + "out.md"), StandardOpenOption.WRITE);
//        outChannel.position(outChannel.size()); // append
        ByteBuffer buf1 = ByteBuffer.allocate(48);
        buf1.clear();
        buf1.put("New String to write to file...".getBytes());
        buf1.flip();
        while(buf1.hasRemaining()) {
            outChannel.write(buf1);
        }
        long fileSize = outChannel.size();
        long position = outChannel.position();
        System.out.println(String.format("fileSize:%s, position:%s", fileSize, position));
//        outChannel.position(0); // overrite
        buf1.clear();
        buf1.put("\nAnother line".getBytes());
        buf1.flip();
        while (buf1.hasRemaining()) {
            outChannel.write(buf1);
        }
        outChannel.close();
    }

    /**
     * 缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存。这块内存被包装成NIO Buffer对象，并提供了一组方法，用来方便的访问该块内存。
     * PROPERTIES:
     * 1. capacity: 作为一个内存块，Buffer有一个固定的大小值，也叫“capacity”, 你只能往里写capacity个byte、long，char等类型。
     *      一旦Buffer满了，需要将其清空（通过读数据或者清除数据）才能继续写数据往里写数据。
     * 2. limit: 读的limit=[0, position], 写的limit=capacity
     * 3. position: 在buffer中读/写的当前位置; 完成一次操作, position移动到下一个读/写位置; position_max=capacity-1
     * METHODS:
     * 1. get(), put(...): get只能获得[postion,limit]的内容
     * 2. flip(): buffer从输入状态转变成输出状态
     * 3. clear()
     * 4. mark()/reset()
     * 5. compact(): 将剩余未读的数据规整到缓冲区头部
     */
    private void bufferDemo() throws IOException {

    }

    /**
     * REGISTER: channel.register(selector, selection-key); SelectionKey表示监听类型, 如connect,accept,read,write, 它们构成"interest集合"
     * METHODS:
     * 1. open()/close(): create/destroy a selector
     * 2. SelectionKey: 每次向selector注册一个channel会返回一个SelectionKey, 之后可以用它来获取channel相关的信息, 也可以继续在它上面附加信息
     *      1) key.interestOps(): Retrieves this key's interest set
     *      2) key.selector(): Returns the selector for which this key was created
     *      3) key.channel(): Returns the channel for which this key was created
     *      4) key.attach(obj): attach data to channel
     *      5) selector.selectedKeys(): Returns a set of SelectionKey
     * 3. selectedKeys(): Returns all the SelectionKey related to a selector
     * 4. select(): 返回的int值表示有多少通道已经就绪。
     *
     * NOTE:
     * 1. 只有非阻塞的channel能和selector结合使用 (selectable channel: SocketChannel, ServerSocketChannel), 而FileChannel无非阻塞模式.
     * 2. Selector使用完关闭: close()
     */
    private void selectorDemo() throws IOException {
        if (true) {
            return;
        }
        SelectableChannel channel = SocketChannel.open();
        Selector selector = Selector.open();
        channel.configureBlocking(false);
        SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
        while(true) {
            int readyChannels = selector.select();
            if(readyChannels == 0) continue;
            Set selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            while(keyIterator.hasNext()) {
                SelectionKey sk = keyIterator.next();
                if(sk.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.
                } else if (sk.isConnectable()) {
                    // a connection was established with a remote server.
                } else if (sk.isReadable()) {
                    // a channel is ready for reading
                } else if (sk.isWritable()) {
                    // a channel is ready for writing
                }
                keyIterator.remove();
            }
        }
    }

    /**
     * TODO
     *
     * @throws IOException
     */
    void ioApi() throws Exception {
        if (true) {
            return;
        }
        // step-01
        InputStream inputStream = Files.newInputStream(Paths.get("..."));
        // step-02
        Reader reader = new InputStreamReader(inputStream);
        // step-03
        BufferedReader bufferedReader = new BufferedReader(reader);

        // step-01
        OutputStream outputStream = Files.newOutputStream(Paths.get("..."));
        // step-02
        Writer writer = new OutputStreamWriter(outputStream);
        // step-03
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        PrintWriter printWriter = new PrintWriter(writer);
    }

    /**
     * TODO
     *
     * @throws IOException
     */
    void socketIo() throws IOException {
        if (true) {
            return;
        }
        URL url = new URL("...");
        URLConnection connection = url.openConnection();

        InputStream in = connection.getInputStream();
        OutputStream out = connection.getOutputStream();
    }
}
