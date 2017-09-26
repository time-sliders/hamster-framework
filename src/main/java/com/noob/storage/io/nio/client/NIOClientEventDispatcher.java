package com.noob.storage.io.nio.client;

import com.noob.storage.io.nio.Dispatcher;

import java.nio.channels.SelectionKey;

/**
 * NIO 客户端SelectionKey 事件转发器
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public class NIOClientEventDispatcher implements Dispatcher<SelectionKey> {

    @Override
    public void dispatch(SelectionKey selectionKey) {

    }
}
