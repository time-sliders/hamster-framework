package com.noob.storage.thread.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;

/**
 * 子线程任务
 *
 * @author luyun
 * @see MultiThreadTask
 * @param <C> 上下文Context
 */
public abstract class SubTask<C> extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(SubTask.class);

    MultiThreadTask<C> mainTask;

    @Override
    public void run() {
        try {
            doBusiness(mainTask.context);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        } finally {
            mainTask.afterSubTaskFinish();
        }
    }

    /**
     * 处理业务逻辑
     */
    protected abstract void doBusiness(C context);

    void setMainTask(MultiThreadTask<C> mainTask) {
        this.mainTask = mainTask;
    }
}
