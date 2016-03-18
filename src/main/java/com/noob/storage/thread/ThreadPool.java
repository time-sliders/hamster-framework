package com.noob.storage.thread;

import com.noob.storage.exception.BusinessException;
import com.noob.storage.exception.ErrorCode;
import com.noob.storage.exception.ProcessException;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 基于java线程池的一个线程池工具类<br/>
 * 主要是为了提供一个等待线程池结束的方法(waitFinish())
 */
public class ThreadPool extends ThreadPoolExecutor {

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
        try {
            waitFinish(Integer.MAX_VALUE, TimeUnit.DAYS);
        } catch (BusinessException e) {
            //dead code
        }
    }

    /**
     * 等待线程池中所有线程运行结束
     *
     * @param timeout 等待的时间
     * @param unit    时间单位
     * @throws BusinessException 在规定的时间内线程池如果没有结束,抛出异常并强制结束线程池
     */
    public void waitFinish(long timeout, TimeUnit unit) throws BusinessException {
        shutdown();
        try {
            awaitTermination(timeout, unit);
            if (!isTerminated()) {
                shutdownNow();
                throw new BusinessException(ErrorCode.POOL_CLOSE_FAIL, "线程池在规定时间内无法结束!");
            }
        } catch (InterruptedException e) {
            throw new ProcessException(e);
        }
    }

}