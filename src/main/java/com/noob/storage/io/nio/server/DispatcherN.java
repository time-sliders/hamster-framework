package com.noob.storage.io.nio.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
 * @since 2019.08.06
 */
public class DispatcherN implements Dispatcher {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherN.class);

    private Selector selector;

    private final Object gate = new Object();

    public DispatcherN() throws IOException {
        selector = Selector.open();
    }

    @Override
    public void run() {
        while (true) {
            try {
                dispatch();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                break;
            }
        }
    }

    private void dispatch() throws IOException {
        selector.select();
        for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext(); ) {
            SelectionKey sk = i.next();
            i.remove();
            Handler handler = (Handler) sk.attachment();
            handler.handle(sk);
        }
        synchronized (gate) {
        }
    }

    @Override
    public void register(SelectableChannel sc, int ops, Handler h)
            throws IOException {
        synchronized (gate) {
            selector.wakeup();
            sc.register(selector, ops, h);
        }
    }
}
