package com.noob.storage.io.nio.server;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * NIO 服务端组件
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public class NIOServer {

    private static final Logger logger = LoggerFactory.getLogger(NIOServer.class);

    /**
     * 服务器端口
     */
    private int port;

    public NIOServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        NIOServer server = new NIOServer(8888);
        server.init();
    }

    /**
     * 启动NIO 服务端
     */
    public void init() {
        try {

            // ～～～～～～～～～～～服务器端初始化～～～～～～～～～～～

            // 1.开启服务端［选择器］
            Selector selector = SelectorProvider.provider().openSelector();
            // 2.开启服务端通道
            ServerSocketChannel ssc = ServerSocketChannel.open();
            // 3.绑定服务端通道端口
            ssc.bind(new InetSocketAddress(port));
            // 4.开启非阻塞模式
            ssc.configureBlocking(false);
            // 5.注册通道到选择器
            ssc.register(selector, SelectionKey.OP_ACCEPT);//初始化时,注册接受连接事件

            logger.info("NIOServer init success @ " + port);

            // ～～～～～～～～～～～等待客户端连接～～～～～～～～～～～
            int n = 0;
            while (true) {
                System.out.println(n++);
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

                    if (sk.isAcceptable()) {
                        // 新的客户端连接进入
                        System.out.println("accept");
                        // 准备接收客户端发送数据
                        SocketChannel socketChannel = ((ServerSocketChannel) sk.channel()).accept();
                        if (socketChannel == null) {
                            continue;
                        }
                        socketChannel.configureBlocking(false);
                        socketChannel.register(sk.selector(), SelectionKey.OP_READ);
                        System.out.println("register@" + socketChannel.toString() + " to selector");
                    } else if (sk.isReadable()) {
                        // 可以从channel中读取数据

                    } else if (sk.isWritable()) {
                        // 可以向channel中写出数据
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}