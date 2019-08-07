package com.noob.storage.io.nio.client;

import com.noob.storage.io.nio.server.Handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
 * @since 2019.08.07
 */
public class WriteHandler implements Handler {
    @Override
    public void handle(SelectionKey sk) throws IOException {
        SocketChannel c = (SocketChannel) sk.channel();
        String msg = "Hello Server";
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(msg.getBytes());
        buffer.flip();
        c.write(buffer);
        c.close();
        sk.cancel();
        System.out.println("client write msg");
    }
}
