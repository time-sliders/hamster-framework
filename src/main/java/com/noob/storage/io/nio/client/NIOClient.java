package com.noob.storage.io.nio.client;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * NIO 客户端
 *
 * @author luyun
 */
public class NIOClient extends Thread {

    private String ip;

    private int port;

    public NIOClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void run() {

        try {
            Selector selector = Selector.open();
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            socketChannel.connect(socketAddress);

            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_CONNECT);

            while (true) {

                if (selector.select() <= 0) {
                    continue;
                }

                Iterator<SelectionKey> i = selector.selectedKeys().iterator();
                while (i.hasNext()) {

                    SelectionKey sk = i.next();
                    i.remove();
                    if (!sk.isValid()) {
                        continue;
                    }

                    SocketChannel sc = (SocketChannel) sk.channel();
                    if (selectionKey.isWritable()) {
                        //2
                        ByteBuffer buffer = ByteBuffer.allocate(1024 * 8);
                        buffer.put("hello,i'am client".getBytes());
                        sc.write(buffer);
                        sk.interestOps(SelectionKey.OP_READ);
                        System.out.println("OP_READ");
                    } else if (selectionKey.isReadable()) {
                        //3
                        System.out.println("READ start");
                        ByteBuffer buffer = ByteBuffer.allocate(1024 * 8);
                        int readNum;
                        StringBuilder sb = new StringBuilder();
                        while ((readNum = sc.read(buffer)) != -1) {
                            buffer.flip();
                            sb.append(new String(buffer.array()));
                            //解析数据
                            byte[] readArray = new byte[readNum];
                            System.arraycopy(buffer.array(), 0, readArray, 0, readNum);
                            sb.append(new String(readArray));

                            buffer.clear();
                        }
                        System.out.println(sb.toString());
                        IOUtils.closeQuietly(sc);
                        System.out.println("END");
                    } else if (selectionKey.isConnectable()) {
                        //1
                        sc.finishConnect();
                        sk.interestOps(SelectionKey.OP_WRITE);
                        System.out.println("OP_WRITE");
                    }

                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
