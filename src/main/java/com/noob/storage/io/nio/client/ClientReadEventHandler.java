package com.noob.storage.io.nio.client;

import com.noob.storage.io.nio.ChannelUtil;
import com.noob.storage.io.nio.NIOEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author luyun
 * @version NIO
 * @since 2017.09.27
 */
public class ClientReadEventHandler extends NIOEventHandler {

    public static final Logger logger = LoggerFactory.getLogger(ClientReadEventHandler.class);

    public ClientReadEventHandler(SelectionKey selectionKey) {
        super(selectionKey);
    }

    @Override
    public void handle(SelectionKey selectionKey) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            if (socketChannel == null) {
                return;
            }

            String s = ChannelUtil.readString(socketChannel);
            System.out.println("Client read:[" + s + "]");

            // 通道关闭
            socketChannel.close();
            // 取消注册
            selectionKey.cancel();
            System.out.println("client cancel from selector");

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }
}
