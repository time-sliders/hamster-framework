package com.noob.storage.io.nio.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author luyun
 */
public class Acceptor extends NIOEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(Acceptor.class);

    private ServerSocketChannel serverSocketChannel;

    public Acceptor(SelectionKey selectionKey, ServerSocketChannel serverSocketChannel) {
        super(selectionKey);
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void run() {
        try {
            System.out.println("Acceptor start" + System.currentTimeMillis());
            SocketChannel socketChannel = serverSocketChannel.accept();

            //非阻塞模式并且没有可用的连接时返回null
            if (socketChannel == null) {
                return;
            }

            socketChannel.configureBlocking(false);
            SelectionKey sk = socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
            sk.attach(new Actor(sk));
            System.out.println("Acceptor end");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
