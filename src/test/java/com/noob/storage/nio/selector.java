package com.noob.storage.nio;

import com.noob.storage.socket.client.SocketDecorate;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author luyun
 * @since 2016.06.17
 */
public class selector {

    public void testSelector() throws Exception{
        Selector selector = Selector.open();
        SocketDecorate socketDecorate = new SocketDecorate("localhost",8080);
        SocketChannel socketChannel  = socketDecorate.getChannel();
        socketChannel.register(selector, SelectionKey.OP_WRITE & SelectionKey.OP_READ);

    }

}
