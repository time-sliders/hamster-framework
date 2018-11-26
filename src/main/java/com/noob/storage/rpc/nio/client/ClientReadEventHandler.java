package com.noob.storage.rpc.nio.client;

import com.noob.storage.rpc.nio.ChannelUtil;
import com.noob.storage.rpc.nio.NIOEventHandler;
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
public class ClientReadEventHandler implements NIOEventHandler {

    public static final Logger logger = LoggerFactory.getLogger(ClientReadEventHandler.class);

    @Override
    public void handle(SelectionKey selectionKey) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            if (socketChannel == null) {
                return;
            }

            String s = ChannelUtil.readString(socketChannel);
            logger.info("Client READ:" + s);

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
