package com.noob.storage.rpc.nio.server;

import com.noob.storage.rpc.nio.NIOEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * 服务器准备写数据到客户端事件处理器
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.27
 */
public class ServerWriteEventHandler implements NIOEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerWriteEventHandler.class);

    @Override
    public void handle(SelectionKey selectionKey) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            String s = (String) selectionKey.attachment();
            String resp = "Hello," + s;

            // 数据写出到通道
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 8);
            buffer.put(resp.getBytes("UTF-8"));
            buffer.flip();

            /*
             * socketChannel 一次数据发送完毕之后,另一方就会触发OP_READ事件
             * 但是某些情况,一个buffer不足以填下所有的数据,这个时候客户端在发
             * 送数据的时候,就需要视情况拆包,即发送多次buffer,同时服务端也要相
             * 应的粘包。
             */
            socketChannel.write(buffer);
            logger.info("Server WRITE >>>" + resp);

            // 通道关闭
            socketChannel.close();
            // 取消注册
            selectionKey.cancel();

        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

    }
}
