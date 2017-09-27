package com.noob.storage.io.nio.client;

import com.noob.storage.io.nio.NIOEventHandler;
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
public class ClientWriteEventHandler extends NIOEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClientWriteEventHandler.class);

    public ClientWriteEventHandler(SelectionKey selectionKey) {
        super(selectionKey);
    }

    @Override
    public void handle(SelectionKey selectionKey) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            String resp = "Hello Server";

            // 数据写出到通道
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 8);
            buffer.put(resp.getBytes("UTF-8"));
            buffer.flip();
            socketChannel.write(buffer);
            System.out.println("client write >>> " + resp);

            selectionKey.interestOps(SelectionKey.OP_READ);

        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

    }

}
