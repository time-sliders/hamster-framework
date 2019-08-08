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
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // TODO channel 复用
        sendMsg("Zhang Wei",buffer,c);
        sendMsg("Huang Shan Shan",buffer,c);
        sendMsg("Huang Shan Shan1",buffer,c);
        sendMsg("Huang Shan Shan23",buffer,c);
        sendMsg("Huang Shan Shan424",buffer,c);
        sendMsg("Huang Shan Shan12",buffer,c);
        sendMsg("111Huang Shan Shan",buffer,c);
        sendMsg("Huang 222 Shan Shan",buffer,c);

        c.close();
        sk.cancel();
        System.out.println("client write msg");
    }

    private void sendMsg(String msg, ByteBuffer buffer, SocketChannel c) throws IOException {
        // TODO 消息编码
        byte[] msgB = msg.getBytes();
        int bodyLength = msgB.length;
        buffer.putLong(bodyLength);
        buffer.put(msgB);// TODO 序列化技术
        buffer.flip();
        c.write(buffer);
        buffer.clear();
    }
}
