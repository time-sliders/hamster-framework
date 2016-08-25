package com.noob.storage.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 基于java线程池的一个线程池工具类<br/>
 * 主要是为了提供一个等待线程池结束的方法(waitFinish())
 */
public class ThreadPool extends ThreadPoolExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPool.class);

    public ThreadPool(int corePoolSize) {
        super(corePoolSize, corePoolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    /**
     * 等待线程池中所有线程运行结束<br/>
     * <p/>
     * <strong>如果线程池中线程一直无法结束,程序会一直等待</strong>
     */
    public void waitFinish() {
        waitFinish(Integer.MAX_VALUE, TimeUnit.DAYS);
    }

    /**
     * 等待线程池中所有线程运行结束
     *
     * @param timeout 等待的时间
     * @param unit    时间单位
     */
    public void waitFinish(long timeout, TimeUnit unit) {

        shutdown();

        try {
            awaitTermination(timeout, unit);//block
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }

        if (!isTerminated()) {
            shutdownNow();
            logger.warn("ThreadPoolExecutor can not shutdown with specify timeout.force shutdownNow.");
        }

    }

}