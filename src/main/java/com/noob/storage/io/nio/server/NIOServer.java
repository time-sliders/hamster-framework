package com.noob.storage.io.nio.server;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * NIO 服务
 * <p>
 * 千里之行,始于足下
 *
 * @author luyun
 */
@Component
public class NIOServer extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(NIOServer.class);

    /**
     * 启动端口
     */
    private int port;

    private ThreadPoolExecutor tpe = null;

    public NIOServer(int port) {
        this.port = port;
    }

    @PostConstruct
    public void init() {

        tpe = new ThreadPoolExecutor(5, 40, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(5000), new RejectedExecutionHandler() {

            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                logger.info("服务器执行任务过多..." + r);
            }
        });

        logger.info("NIO Server ThreadPoolExecutor init succ!");
    }

    @Override
    public void run() {
        Selector selector = null;
        try {
            /*
             * 选择器:
             * Selector（选择器）是Java NIO中能够检测一到多个NIO通道，
             * 并能够知晓通道是否为诸如读写事件做好准备的组件。这样，一个
             * 单独的线程可以管理多个 channel,从而管理多个网络连接。
             */
            selector = Selector.open();
            SocketAddress address = new InetSocketAddress(port);
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.socket().bind(address);
            SelectionKey selectionKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(new Acceptor(selectionKey, ssc));

            while (true) {

                /*
                 * 阻塞
                 * 等待selector 上至少有一个selectionKey准备就绪
                 */
                if (selector.select() <= 0) {
                    continue;
                }

                //获取已经准备就绪的selectionKey set
                Iterator<SelectionKey> ski = selector.selectedKeys().iterator();

                while (ski.hasNext()) {

                    SelectionKey sk = ski.next();

                    ski.remove();

                    if (!sk.isValid()) {
                        continue;
                    }

                    tpe.submit((Runnable) selectionKey.attachment());
                }
            }
        } catch (IOException e) {
            logger.error(null, e);
        } finally {
            IOUtils.closeQuietly(selector);
        }
    }

}
