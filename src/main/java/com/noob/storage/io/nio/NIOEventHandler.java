package com.noob.storage.io.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;

/**
 * NIO 事件处理器
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public abstract class NIOEventHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NIOEventHandler.class);

    private SelectionKey selectionKey;

    public NIOEventHandler(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    @Override
    public void run() {
        try {
            handle(selectionKey);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    public abstract void handle(SelectionKey selectionKey);
}
