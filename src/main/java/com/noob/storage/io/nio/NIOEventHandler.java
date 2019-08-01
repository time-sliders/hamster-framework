package com.noob.storage.io.nio;

import java.nio.channels.SelectionKey;

/**
 * NIO 事件处理器
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public interface NIOEventHandler {

    void handle(SelectionKey selectionKey);
}
