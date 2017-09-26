package com.noob.storage.io.nio.client;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    private static ThreadPoolExecutor tpe;

    static {
        tpe = new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                logger.warn("------------------reject>>>>" + JSON.toJSONString(r));
            }
        });
        tpe.allowCoreThreadTimeOut(true);
    }

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
            Selector selector = SelectorProvider.provider().openSelector();
            // 2.开启客户端channel
            SocketChannel sc = SocketChannel.open();
            // 3.绑定服务端地址
            sc.connect(new InetSocketAddress(serverIp, port));
            // 4.设置非绑定模式
            sc.configureBlocking(false);
            // 5.注册到选择器
            sc.register(selector, SelectionKey.OP_CONNECT);

            logger.info("NIOClient init success @ " + serverIp + ":" + port);

            // ～～～～～～～～～～～客户端业务处理～～～～～～～～～～
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

                    if (sk.isConnectable()) {
                        // 可以连接到服务器
                        System.out.println("connected");
                    } else if (sk.isWritable()) {
                        // 可以写数据
                    } else if (sk.isReadable()) {
                        // 可以读数据
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
