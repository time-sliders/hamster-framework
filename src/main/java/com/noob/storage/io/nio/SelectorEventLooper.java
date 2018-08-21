package com.noob.storage.io.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * @author luyun
 * @version NIO
 * @since 2017.09.27
 */
public class SelectorEventLooper {

    public static void loop(Selector selector, Dispatcher<SelectionKey> dispatcher) throws IOException {

        while (true) {

            if (selector.select() <= 0) {
                continue;
            }

            Iterator<SelectionKey> i = selector.selectedKeys().iterator();

            while (i.hasNext()) {
                SelectionKey sk = i.next();
                i.remove();
                if (!sk.isValid()) {
                    continue;
                }

                dispatcher.dispatch(sk);
            }
        }
    }
}
