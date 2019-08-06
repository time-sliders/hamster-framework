package com.noob.storage.io.nio.server;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Acceptor.class);

    private ServerSocketChannel ssc;
    private DispatcherN d;
    private HandlerAdapter ha;// SPI

    public Acceptor(ServerSocketChannel ssc, DispatcherN d) {
        this.ssc = ssc;
        this.d = d;
        ha = ExtensionLoader
                .getExtensionLoader(HandlerAdapter.class)
                .getAdaptiveExtension();
    }

    @Override
    public void run() {

        for (; ; ) {
            try {
                SocketChannel sc = ssc.accept();
                d.register(sc, SelectionKey.OP_READ, ha);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                break;
            }
        }
    }
}
