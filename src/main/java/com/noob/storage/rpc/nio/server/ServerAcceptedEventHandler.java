package com.noob.storage.rpc.nio.server;

import com.noob.storage.rpc.nio.NIOEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 客户端连接事件处理器
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public class ServerAcceptedEventHandler implements NIOEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerAcceptedEventHandler.class);

    @Override
    public void handle(SelectionKey selectionKey) {
        try {
            SocketChannel sc = ((ServerSocketChannel) selectionKey.channel()).accept();
            if (sc == null) {
                return;
            }
            sc.configureBlocking(false);
            sc.register(selectionKey.selector(), SelectionKey.OP_READ);
            logger.info("服务端收到连接请求");
        } catch (IOException e) {
            logger.warn("客户端连接处理异常", e);
        }
    }
}
