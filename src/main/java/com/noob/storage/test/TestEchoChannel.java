package com.noob.storage.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author 卢云(luyun)
 * @since 2019.09.05
 */
public class TestEchoChannel {

    public static void main(String[] args) throws IOException {
        ReadableByteChannel rc = Channels.newChannel(System.in);
        WritableByteChannel wc = Channels.newChannel(System.out);

        channelCopy(rc, wc);

        rc.close();
        wc.close();
    }

    private static void channelCopy(ReadableByteChannel rc, WritableByteChannel wc) throws IOException {

        ByteBuffer byteBuffer = ByteBuffer.allocate(16 * 1024);
        while (rc.read(byteBuffer) != -1) {
            byteBuffer.flip();
            wc.write(byteBuffer);
            byteBuffer.compact();
        }

        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            wc.write(byteBuffer);
        }
    }
}
