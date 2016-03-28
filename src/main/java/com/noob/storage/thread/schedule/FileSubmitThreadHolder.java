package com.noob.storage.thread.schedule;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 文件提交线程持有者
 */
public abstract class FileSubmitThreadHolder extends ThreadHolder {

    /**
     * 文件的总大小
     */
    protected int totalSize = 0;

    private static final int maxCacheSize = 10;

    /**
     * 最近maxCacheSize次传输速度快照
     */
    protected Deque<Integer> snapshot = new LinkedList<Integer>();

    public FileSubmitThreadHolder(ThreadPoolExecutor pool) {
        super(pool);
    }

    public FileSubmitThreadHolder(int needForReSource, ThreadPoolExecutor pool) {
        super(needForReSource, pool);
    }

    /**
     * 在单位时间提交的文件大小与提交线程数计算资源渴望度
     */
    @Override
    protected void calculateNeed() {

        this.proportion = totalSize / holdNum;

        if (snapshot.size() >= maxCacheSize) {
            snapshot.removeLast();
            snapshot.addFirst(proportion);
        }

        totalSize = 0;
    }

    protected synchronized void totalSizePlus(int size) {
        totalSize += size;
    }

}