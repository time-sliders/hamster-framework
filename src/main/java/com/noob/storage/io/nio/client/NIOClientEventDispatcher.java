package com.noob.storage.io.nio.client;

import com.alibaba.fastjson.JSON;
import com.noob.storage.io.nio.Dispatcher;
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

    private static ThreadPoolExecutor tpe;

    static {
        tpe = new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                logger.warn("------------------reject>>>>" + JSON.toJSONString(r));
            }
        });
        tpe.allowCoreThreadTimeOut(true);
    }

    @Override
    public void dispatch(SelectionKey sk) {
        if (sk.isConnectable()) {
            new ClientConnectEventHandler(sk).run();
        } else if (sk.isWritable()) {
            new ClientWriteEventHandler(sk).run();
        } else if (sk.isReadable()) {
            new ClientReadEventHandler(sk).run();
        }
    }
}
