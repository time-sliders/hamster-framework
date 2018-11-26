package com.noob.storage.rpc.nio.server;

import com.noob.storage.rpc.nio.SelectorEventLooper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * NIO 服务端组件
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public class NIOServer {

    private static final Logger logger = LoggerFactory.getLogger(NIOServer.class);

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
            SelectorEventLooper.loop(selector, new NIOServerEventDispatcher());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}