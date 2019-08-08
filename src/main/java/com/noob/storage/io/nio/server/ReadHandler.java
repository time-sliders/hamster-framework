package com.noob.storage.io.nio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author 卢云(luyun)
 * @since 2019.08.07
 */
public class ReadHandler implements Handler {

    @Override
    public void handle(SelectionKey sk) {
        try {
            SocketChannel sc = (SocketChannel) sk.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            sc.read(buffer);
            buffer.flip();
            while (buffer.hasRemaining()) {
                receiveMsg(buffer);
            }
            buffer.clear();
            sc.close();
            sk.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMsg(ByteBuffer buffer) {
        // TODO 消息解码
        long bodyLength = buffer.getLong(buffer.position());
        buffer.position(buffer.position() + 8);
        System.out.println("body length = " + bodyLength);
        System.out.println(new String(buffer.array(), buffer.position(), (int) bodyLength)); // // TODO 反序列化技术
        buffer.position((int)(buffer.position() + bodyLength));
    }
}
