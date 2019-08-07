package com.noob.storage.io.nio.client;

import com.noob.storage.io.nio.server.Handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
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
        System.out.println((sk.interestOps() & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT);
        System.out.println((sk.interestOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ);
        System.out.println((sk.interestOps() & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE);
    }
}
