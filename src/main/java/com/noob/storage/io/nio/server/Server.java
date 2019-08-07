package com.noob.storage.io.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * NIO 服务端组件
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public class Server extends AbstractSelectorLoop {

    public static void main(String[] args) throws IOException {
        new Server().runServer();
    }

    public void runServer() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8888));
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT, new AcceptHandler());
        loopSelector(selector);
    }
}