package com.noob.storage.io.nio.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
 * @since 2019.08.06
 */
public interface Handler {

    void handle(SelectionKey sk) throws IOException;
}
