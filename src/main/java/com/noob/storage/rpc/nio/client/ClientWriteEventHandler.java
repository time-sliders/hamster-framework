package com.noob.storage.rpc.nio.client;

import com.noob.storage.rpc.nio.NIOEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author luyun
 * @version NIO
 * @since 2017.09.27
 */
public class ClientWriteEventHandler implements NIOEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClientWriteEventHandler.class);

    @Override
    public void handle(SelectionKey selectionKey) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            String msg = "NIO";
            // 数据写出到通道
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 8);
            buffer.put(msg.getBytes("UTF-8"));
            buffer.flip();
            socketChannel.write(buffer);
            logger.info("client write [" + msg + "]");
            selectionKey.interestOps(SelectionKey.OP_READ);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

}
