package com.noob.storage.io.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * 服务端SelectionKey的Attachment
 *
 * @author luyun
 */
public class ServerAttachment implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ServerAttachment.class);

    private SelectionKey selectionKey;

    public ServerAttachment(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    @Override
    public void run() {

        try {

            Set<SelectionKey> selectionKeySet = selectionKey.selector().selectedKeys();
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            socketChannel.configureBlocking(false);

        } catch (IOException e) {
            logger.error("处理客户端连接失败", e);
        }
    }
}
