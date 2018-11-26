package com.noob.storage.rpc.nio.client;

import com.noob.storage.rpc.nio.SelectorEventLooper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * NIO 客户端
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public class NIOClient {

    private static final Logger logger = LoggerFactory.getLogger(NIOClient.class);

    /**
     * 服务器IP地址
     */
    private String serverIp;

    /**
     * 服务器端口
     */
    private int port;

    private Selector selector;

    public NIOClient(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public static void main(String[] args) {
        NIOClient nioClient = new NIOClient("localhost", 8888);
        nioClient.init();
    }

    public void init() {
        try {

            // ～～～～～～～～～～～客户端初始化～～～～～～～～～～～

            // 1.开启客户端选择器
            selector = SelectorProvider.provider().openSelector();

            logger.info("NIOClient init success @ " + serverIp + ":" + port);

            register();

            // ～～～～～～～～～～～客户端业务处理～～～～～～～～～～
            SelectorEventLooper.loop(selector, new NIOClientEventDispatcher());

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void register() {
        try {
            // 2.开启客户端channel
            SocketChannel sc = SocketChannel.open();
            // 4.设置非模式
            sc.configureBlocking(false);
            // 3.绑定服务端地址
            sc.connect(new InetSocketAddress(serverIp, port));
            // 5.注册到选择器
            sc.register(selector, SelectionKey.OP_CONNECT);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }


}
