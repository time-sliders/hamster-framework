package com.noob.storage.io.nio.server;

import java.io.IOException;
import java.nio.channels.SelectableChannel;

public interface Dispatcher extends Runnable {

    void register(SelectableChannel sc, int ops, Handler h)
            throws IOException;
}
