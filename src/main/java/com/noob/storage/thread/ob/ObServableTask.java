package com.noob.storage.thread.ob;

import org.apache.log4j.Logger;

/**
 * 线程池运行的任务
 */
public abstract class ObServableTask implements Runnable {

    private Logger log = Logger.getLogger(getClass());

    protected Counter counter;

    private final Thread mainThread;

    public ObServableTask() {
        mainThread = Thread.currentThread();
    }

    public void run() {
        try {
            execute();
        } catch (Throwable e) {
            log.error("ObservableTask occur exception", e);
        } finally {
            if (counter.isFinish()) {
                notifyMainThread();
            }
        }
    }

    /**
     * 唤醒主线程
     */
    private void notifyMainThread() {
        synchronized (mainThread) {
            mainThread.notify();
        }
    }

    /**
     * 子线程中需要执行的任务
     */
    public abstract void execute();

    public void setCounter(Counter counter) {
        this.counter = counter;
    }
}
