package com.noob.storage.resource;

import com.noob.storage.resource.base.InitAble;
import com.noob.storage.thread.ThreadPool;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 资源探测器，从一组资源中检测出一个可用的资源
 */
public class ResourceDetector {

    /**
     * 待检测的资源
     */
    private List<? extends InitAble> resources;

    /**
     * 创建该对象的线程
     */
    private final Thread mainThread;

    /**
     * 探测结果
     */
    private InitAble result;

    /**
     * 用于并发探测可用资源的线程池
     * @see java.util.concurrent.ExecutorService
     */
    private ThreadPool pool;

    private static final int DEFAULT_DETECT_POOL_SIZE = 5;

    /**
     * 探测失败的记录数
     */
    private Integer failCount = 0;

    /**
     * @param resources        待探测的资源
     * @param detectorPoolSize 同时起多少个线程检测
     */
    public ResourceDetector(List<? extends InitAble> resources, int detectorPoolSize) {
        super();
        this.resources = resources;
        this.mainThread = Thread.currentThread();
        if (detectorPoolSize <= 0 || detectorPoolSize > 10) {
            detectorPoolSize = DEFAULT_DETECT_POOL_SIZE;
        }
        pool = new ThreadPool(detectorPoolSize);
    }

    /**
     * 探测资源
     */
    public InitAble detect() {

        if (CollectionUtils.isEmpty(resources)) {
            return null;
        }

        for (InitAble i : resources) {
            pool.execute(new DetectTask(i));
        }

        synchronized (mainThread) {
            try {
                mainThread.wait();
            } catch (InterruptedException ignored) {
            }
        }

        pool.shutdownNow();
        return result;
    }


    /**
     * 探测任务
     * <p>每一个任务负责检测一个资源</p>
     */
    private class DetectTask implements Runnable {

        InitAble i;

        DetectTask(final InitAble i) {
            this.i = i;
        }

        public void run() {
            try {
                if (i.init()) {
                    result = i;
                    notifyMainThread();
                } else {
                    taskFail();
                }
            } finally {
                i.destroy();
            }
        }

    }

    /**
     * 任务失败<br/>
     * 当所有任务失败之后,唤醒主线程
     */
    private synchronized void taskFail() {
        if (++failCount >= resources.size()) {
            notifyMainThread();
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

}
