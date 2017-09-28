package com.java.api.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by kevintian on 2017/9/27.
 */
public class NativeIODemo {
    final String FILE_ROOT = System.getProperty("user.home") + "/Desktop/io_test/";

    public static void main(String[] args) throws Exception {
        NativeIODemo demo = new NativeIODemo();
        demo.ioApi();
        demo.nioApi();
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
        filesDemo();
    }

    /**
     * CONTENT: 1-7
     */
    void filesDemo() throws IOException {
        Path path = Paths.get(FILE_ROOT, "test.txt");
        // 1. create file
        if (!Files.exists(path)) {
            System.out.println("file not exists");
            Path parent = path.getParent();
            if (!Files.exists(parent)) {
                Files.createDirectory(parent);
            }
            Files.createFile(path);
        }
        // 2. write
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (int i=0; i<100; i++) {
                writer.write(String.format("第%s行: %s\n", i, String.valueOf(Math.random())));
            }
            writer.flush();
        }
        // 3. read
        Files.lines(path, Charset.forName("utf-8")).forEach(line -> System.out.println(line));
        // 4. copy
        Path cp = Paths.get(FILE_ROOT, "test_cp.txt");
        Files.copy(path, cp, StandardCopyOption.REPLACE_EXISTING);
        // 5. move
        Path target = Paths.get(FILE_ROOT, "test_mv.txt");
        Files.move(path, target, StandardCopyOption.ATOMIC_MOVE);
        // 6. delete
        Path toDel = Files.copy(cp, Paths.get(FILE_ROOT, "test_redundant.txt"));
        boolean isDel = Files.deleteIfExists(toDel);
        System.out.println("is deleted: " + isDel);
        // 1b. create tempt file
        Files.list(path.getParent()).forEach(oldTmp -> {
            if (oldTmp.getFileName().toString().contains("tmp")) {
                try {
                    System.out.println("delete old tmp:" + oldTmp.getFileName());
                    Files.delete(oldTmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Path tmp = Files.createTempFile(path.getParent(), "tmp", ".txt");
        try(PrintWriter printer = new PrintWriter(Files.newOutputStream(tmp))){
            printer.write("this is a tempt file written by 'PrintWriter'");
            printer.flush();
        }
        // 7. list files
        Files.walk(path.getParent(), 1).forEach(p -> System.out.println(p.toString()));
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
        // refer to 'socketIO()'
    }

    /**
     * FileChannel
     *      1) transferFrom(): read data from other channel
     *      2) transferTo(): write data to other channel
     *      3) postion(): operate position in the related file
     *      4) size(): size of the related file
     *      5) lock(): get 'FileLock' as multiple program trying to operate on a same file
     * CONSTRUCTION: from InputStream, OutputStream, RandomAccessFile
     * @throws IOException
     */
    private void fileChannelDemo() throws IOException {
        // read
        RandomAccessFile aFile = new RandomAccessFile(FILE_ROOT + "elk学习测试.md", "r");
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
        Path path = Paths.get(FILE_ROOT + "out.md");
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        FileChannel outChannel = FileChannel.open(path, StandardOpenOption.WRITE);
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
//        outChannel.position(0); // overwrite
        buf1.clear();
        buf1.put("\nAnother line".getBytes());
        buf1.flip();
        while (buf1.hasRemaining()) {
            outChannel.write(buf1);
        }
        outChannel.close();
    }

    /**
     * 缓冲区本质上是一块供读/写数据的内存。这块内存被包装成NIO Buffer对象，并提供了一组方法，用来方便的访问该块内存。
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
        // refer to 'fileChannelDemo()'
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
        // refer to 'socketIO()'
        /* template
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
        */
    }

    /**
     * INPUT(3 LEVELS):
     * 1. InputStream
     * 2. Reader
     * 3. children class of Reader, e.g. BufferedReader
     *
     * OUTPUT(3 LEVELS):
     * 1. OutputStream
     * 2. Writer
     * 3. children class of Writer, e.g. BufferedWriter, PrintWriter(more powerful)
     *
     * @throws IOException
     */
    void ioApi() throws Exception {
        // refer to 'httpIO()'
    }

    /**
     * CONTENT:
     * 1. input(do Get)
     * 2. output(do POST)
     *
     * @throws IOException
     */
    void httpIO() throws IOException {
        URLInput();
        URLOutput();
    }

    /**
     * do GET: use the basic URLConnection
     *
     * @throws IOException
     */
    private void URLInput() throws IOException {
        URL url = new URL("https://www.baidu.com");
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String content = reader.readLine();
            while (content != null) {
                System.out.println(content);
                content = reader.readLine();
            }
        }
        Map<String, List<String>> headers = connection.getHeaderFields();
        for (Map.Entry<String, List<String>> hd : headers.entrySet()) {
            System.out.println(String.format("head: key=%s, value=%s", hd.getKey(), hd.getValue()));
        }
    }

    /**
     * do POST: use HttpURLConnection
     *
     * @throws IOException
     */
    private void URLOutput() throws IOException {
        URL target = new URL("http://localhost:8088/rest/jpa_entity");
        HttpURLConnection httpConn = (HttpURLConnection) target.openConnection();
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Accept-Charset", "utf-8");
        Map<String, Object> bdParams = new HashMap<>();
        bdParams.put("fieldA", "urirest-test fA");
        bdParams.put("fieldB", "urirest-test fB");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpConn.getOutputStream()))) {
            writer.write(new ObjectMapper().writeValueAsString(bdParams));
            writer.flush();
        }
        System.out.println("status: "+ httpConn.getResponseCode());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()))) {
            String response = reader.readLine();
            while (response != null) {
                System.out.println(String.format(response));
                response = reader.readLine();
            }
        }
    }

    /**
     * COMPONENT:
     * 1. socket server
     * 2. socket client
     */
    void socketIO() {
        System.out.println(StringUtils.center("socketIO", 100, "="));
        Executor exec = Executors.newFixedThreadPool(2);
        exec.execute(new NioSocketServer());
        exec.execute(new NioSocketClient());
    }
}
