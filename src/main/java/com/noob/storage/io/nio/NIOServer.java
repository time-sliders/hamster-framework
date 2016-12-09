package com.noob.storage.io.nio;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * NIO 服务
 * <p>
 * 千里之行,始于足下
 *
 * @author luyun
 */
public class NIOServer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NIOServer.class);

    /**
     * 启动端口
     */
    private int port;

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
            ssc.bind(address);
            SelectionKey selectionKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
            selectionKey.attach(new ServerAttachment(selectionKey));

            while (true) {

                /*
                 * 阻塞
                 * 等待selector 上至少有一个selectionKey准备就绪
                 */
                selector.select();

                /*
                 * 获取已经准备就绪的selectionKey set
                 */
                Iterator<SelectionKey> ski = selector.selectedKeys().iterator();

                while (ski.hasNext()) {

                    SelectionKey sk = ski.next();

                    ski.remove();

                    if (!sk.isValid()) {
                        continue;
                    }

                    dispatch(sk);
                }
            }
        } catch (IOException e) {
            logger.error(null, e);
        } finally {
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException ioe) {
                    // ignored
                }
            }
        }
    }

}
