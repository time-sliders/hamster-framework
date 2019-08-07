package com.noob.storage.io.nio.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
 * @since 2019.08.07
 */
public class AcceptHandler implements Handler {

    private ReadHandler readHandler = new ReadHandler();

    @Override
    public void handle(SelectionKey sk) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        sc.register(sk.selector(), SelectionKey.OP_READ, readHandler);
    }
}
