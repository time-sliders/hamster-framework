package com.noob.storage.io.nio.server;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.noob.storage.io.nio.ChannelUtil;
import com.noob.storage.io.nio.NIOEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * 服务端socketChannel已经准备好读取事件的处理器
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public class ServerReadEventHandler extends NIOEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerReadEventHandler.class);

    public ServerReadEventHandler(SelectionKey selectionKey) {
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
            if (StringUtils.isBlank(s)) {
                socketChannel.close();
                selectionKey.cancel();
                return;
            }

            logger.info("Server READ:" + s);
            selectionKey.attach(s);

            selectionKey.interestOps(SelectionKey.OP_WRITE);

        } catch (Exception e) {
            logger.error("服务端读取数据异常", e);
        }
    }
}
