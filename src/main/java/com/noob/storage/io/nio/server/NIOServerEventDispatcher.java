package com.noob.storage.io.nio.server;

import com.noob.storage.io.nio.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;

/**
 * NIO服务端事件处理器
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public class NIOServerEventDispatcher implements Dispatcher<SelectionKey> {

    private static final Logger logger = LoggerFactory.getLogger(NIOServerEventDispatcher.class);

    @Override
    public void dispatch(SelectionKey sk) {
        if (sk.isAcceptable()) {
            new ServerAcceptedEventHandler().handle(sk);
        } else if (sk.isReadable()) {
            new ServerReadEventHandler().handle(sk);
        } else if (sk.isWritable()) {
            new ServerWriteEventHandler().handle(sk);
        } else {
            logger.info("unknownSelectionKeyType");
        }

    }
}
