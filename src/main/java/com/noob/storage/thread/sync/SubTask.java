package com.noob.storage.thread.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;

/**
 * 子线程任务
 *
 * @author luyun
 * @see MultiThreadTask
 */
public abstract class SubTask extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(SubTask.class);

    MultiThreadTask mainTask;

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
    protected abstract void doBusiness(ConcurrentMap<String, Object> context);

    void setMainTask(MultiThreadTask mainTask) {
        this.mainTask = mainTask;
    }
}
