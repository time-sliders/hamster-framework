package com.noob.storage.io.nio.server;

import java.nio.channels.SelectionKey;

/**
 * NIO事件处理器
 *
 * @author luyun
 * @since 2017.01.21 (fund portfolio pre)
 */
public abstract class NIOEventHandler implements Runnable {

    protected SelectionKey selectionKey;

    public NIOEventHandler(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

}
