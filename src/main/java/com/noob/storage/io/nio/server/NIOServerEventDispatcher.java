package com.noob.storage.io.nio.server;

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
 * NIO服务端事件处理器
 *
 * @author luyun
 * @version NIO
 * @since 2017.09.26
 */
public class NIOServerEventDispatcher implements Dispatcher<SelectionKey> {

    private static final Logger logger = LoggerFactory.getLogger(NIOServerEventDispatcher.class);

    private static ThreadPoolExecutor tpe;

    static {
        tpe = new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                logger.warn("------------------reject>>>>" + JSON.toJSONString(r));
            }
        });
        tpe.allowCoreThreadTimeOut(true);
    }

    @Override
    public void dispatch(SelectionKey selectionKey) {

    }
}
