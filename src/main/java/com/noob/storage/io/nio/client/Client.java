package com.noob.storage.io.nio.client;

import com.noob.storage.io.nio.server.AbstractSelectorLoop;
import com.noob.storage.io.nio.server.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * NIO 客户端
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public class Client extends AbstractSelectorLoop {

    public static void main(String[] args) throws IOException {
        new Client().runClient();
    }

    public void runClient() throws IOException {
        Selector selector = SelectorProvider.provider().openSelector();
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(new InetSocketAddress("localhost", 8888));
        sc.register(selector, SelectionKey.OP_CONNECT, new ConnectedHandler());
        loopSelector(selector);
    }

}
