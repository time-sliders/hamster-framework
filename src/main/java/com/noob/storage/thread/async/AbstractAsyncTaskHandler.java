package com.noob.storage.thread.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务处理器
 * <p>
 * 需要特别注意，不允许在更新T 的Transactional中，提交T的异步处理任务
 * 因为事务没有提交的情况下，另一个线程是无法获取最新的数据的。
 *
 * @param <T> 业务数据,一般可以是订单ID等
 * @author luyun
 * @version 理财计划
 * @since 2017.11.02
 */
public abstract class AbstractAsyncTaskHandler<T> implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAsyncTaskHandler.class);

    /**
     * 异步线程池
     */
    private ThreadPoolExecutor tpe;


    /**
     * 提交业务数据到异步线程池
     * <p>
     * 禁止开启事务
     *
     * @param t 业务数据
     */
    public void submitToAsyncPool(T t) {
        tpe.submit(new AsyncHandleTask(t));
    }

    /**
     * 异步任务
     */
    private class AsyncHandleTask implements Runnable {
        private T t;

        AsyncHandleTask(T t) {
            this.t = t;
        }

        @Override
        public void run() {
            try {
                handle(t);
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 业务数据异步处理逻辑
     */
    protected abstract void handle(T t);

    /**
     * 初始化异步处理线程池
     */
    protected void initAsyncPool() {
        tpe = new ThreadPoolExecutor(4, 4,
                10, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    }


    @Override
    public final void afterPropertiesSet() throws Exception {
        initAsyncPool();
        tpe.allowCoreThreadTimeOut(true);
    }


}
