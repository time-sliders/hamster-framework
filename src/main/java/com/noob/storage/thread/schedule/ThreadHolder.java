package com.noob.storage.thread.schedule;

import java.util.concurrent.ThreadPoolExecutor;

public abstract class ThreadHolder extends Holder {

    protected ThreadPoolExecutor pool;

    public ThreadHolder(ThreadPoolExecutor pool) {
        super();
        this.pool = pool;
    }

    public ThreadHolder(int needForReSource, ThreadPoolExecutor pool) {
        super(needForReSource);
        this.pool = pool;
    }

    @Override
    protected void reSize(int num) {
        pool.setCorePoolSize(num);
        pool.setMaximumPoolSize(num);
    }

    @Override
    protected abstract void calculateNeed();

}
