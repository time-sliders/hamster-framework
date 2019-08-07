package com.noob.storage.io.nio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author 卢云(luyun)
 * @version app 7.8.3
 * @since 2019.08.07
 */
public class ReadHandler implements Handler {
    @Override
    public void handle(SelectionKey sk) {
        try {
            SocketChannel sc = (SocketChannel) sk.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = sc.read(buffer); // TODO decode
            System.out.println(read);
            buffer.flip();
            System.out.println(new String(buffer.array(), 0, read));
            sc.close();
            sk.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
