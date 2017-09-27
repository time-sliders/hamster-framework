package com.noob.storage.io.nio.server;

import com.noob.storage.io.nio.NIOEventHandler;
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
public class ServerAcceptedEventHandler extends NIOEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerAcceptedEventHandler.class);

    public ServerAcceptedEventHandler(SelectionKey selectionKey) {
        super(selectionKey);
    }

    @Override
    public void handle(SelectionKey selectionKey) {
        try {
            SocketChannel sc = ((ServerSocketChannel) selectionKey.channel()).accept();
            if (sc == null) {
                return;
            }
            sc.register(selectionKey.selector(), SelectionKey.OP_READ);
            sc.configureBlocking(false);
        } catch (IOException e) {
            logger.warn("客户端连接处理异常", e);
        }
    }
}
