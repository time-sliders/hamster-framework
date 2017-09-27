package com.noob.storage.io.nio.client;

import com.noob.storage.io.nio.NIOEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author luyun
 * @version NIO
 * @since 2017.09.27
 */
public class ClientConnectEventHandler extends NIOEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnectEventHandler.class);

    public ClientConnectEventHandler(SelectionKey selectionKey) {
        super(selectionKey);
    }

    @Override
    public void handle(SelectionKey sk) {

        SocketChannel sc = (SocketChannel) sk.channel();

        try {
            sc.finishConnect();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            sk.cancel();
        }

        sk.interestOps(SelectionKey.OP_WRITE);
    }
}
