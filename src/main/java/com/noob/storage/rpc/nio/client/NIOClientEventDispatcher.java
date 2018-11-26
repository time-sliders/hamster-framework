package com.noob.storage.rpc.nio.client;

import com.alibaba.fastjson.JSON;
import com.noob.storage.rpc.nio.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * NIO 客户端SelectionKey 事件转发器
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public class NIOClientEventDispatcher implements Dispatcher<SelectionKey> {

    private static final Logger logger = LoggerFactory.getLogger(NIOClientEventDispatcher.class);

    @Override
    public void dispatch(SelectionKey sk) {
        if (sk.isConnectable()) {
            new ClientConnectEventHandler().handle(sk);
        } else if (sk.isWritable()) {
            new ClientWriteEventHandler().handle(sk);
        } else if (sk.isReadable()) {
            new ClientReadEventHandler().handle(sk);
        } else {
            logger.info("unknownSelectionKeyType");
        }
    }
}
