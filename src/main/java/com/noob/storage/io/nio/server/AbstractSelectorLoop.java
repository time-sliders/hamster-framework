package com.noob.storage.io.nio.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
 * @since 2019.08.07
 */
public class AbstractSelectorLoop {

    protected void loopSelector(Selector selector){
        for (; ; ) {
            try {
                selector.select();
                for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext(); ) {
                    SelectionKey sk = i.next();
                    i.remove();
                    Handler handler = (Handler) sk.attachment();
                    handler.handle(sk);// TODO async
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
