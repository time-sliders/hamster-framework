package com.noob.storage.io.nio.client;

import com.noob.storage.io.nio.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;

/**
 * NIO 客户端SelectionKey 事件转发器
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public class NIOClientEventDispatcher implements Dispatcher<SelectionKey> {

    private static final Logger logger = LoggerFactory.getLogger(NIOClientEventDispatcher.class);

    @Override
    public void dispatch(SelectionKey sk) {
        if (sk.isConnectable()) {
            new ClientConnectEventHandler().handle(sk);
        } else if (sk.isWritable()) {
            new ClientWriteEventHandler().handle(sk);
        } else if (sk.isReadable()) {
            new ClientReadEventHandler().handle(sk);
        } else {
            logger.info("unknownSelectionKeyType");
        }
    }
}
