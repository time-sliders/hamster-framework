package com.noob.storage.io.nio.client;

import com.noob.storage.io.nio.server.Handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author 卢云(luyun)
 * @since 2019.08.07
 */
public class ConnectedHandler implements Handler {

    private WriteHandler writeHandler = new WriteHandler();

    @Override
    public void handle(SelectionKey sk) throws IOException {
        SocketChannel sc = (SocketChannel) sk.channel();
        sc.finishConnect();
        sk.interestOps(SelectionKey.OP_WRITE);
        sk.attach(writeHandler);
    }
}
