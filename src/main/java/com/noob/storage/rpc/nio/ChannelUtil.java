package com.noob.storage.rpc.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author luyun
 * @version NIO
 * @since 2017.09.27
 */
public class ChannelUtil {

    public static String readString(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 8);
        int index = socketChannel.read(buffer);
        buffer.flip();
        if (index > 0) {
            return new String(buffer.array(), 0, index, "UTF-8");
        }
        return null;
    }
}
