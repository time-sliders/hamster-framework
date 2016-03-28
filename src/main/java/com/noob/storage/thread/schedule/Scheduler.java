package com.noob.storage.thread.schedule;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scheduler extends Thread {

    private Logger log = Logger.getLogger(getClass());

    private int maxNum;

    private List<Holder> holders = Collections.synchronizedList(new ArrayList<Holder>());

    private static final int DEFAULT_SLEEP_TIME = 1000 * 60;

    /**
     * 分配策略
     * 在调度者注册或者释放资源持有对象的时候，根据该策略来重新分配任务
     * 调度器启动之后也会每隔一段时间自动重新分配
     */
    private ResourceAllocatePolicy allocatePolicy;

    private static final ResourceAllocatePolicy defaultAllocatePolicy
            = new ProportionAllocatePolicy();

    public Scheduler(int maxNum) {
        this(maxNum, defaultAllocatePolicy);
    }

    public Scheduler(int maxNum, ResourceAllocatePolicy allocatePolicy) {
        this.maxNum = maxNum;
        this.allocatePolicy = allocatePolicy;
    }

    /**
     * 根据指定的分配策略重新分配资源
     */
    private synchronized void reAllocate() {
        allocatePolicy.allocate(maxNum, holders);
    }

    /**
     * 注册一个资源持有者并分配资源
     */
    public void register(Holder holder) {
        if (holder == null) {
            throw new NullPointerException();
        }
        if (holder.getProportion() <= 0) {
            throw new IllegalArgumentException("holder.proportion must not be null!");
        }
        if (holders.add(holder)) {
            holder.setScheduler(this);
            reAllocate();
        }
    }

    /**
     * 释放一个调度器所持有的资源，并重新分配给其他持有者
     */
    public void release(Holder holder) {
        if (holder != null && holders.remove(holder)) {
            reAllocate();
        }
    }

    /**
     * 调度器每隔一段时间动态分配资源
     */
    public void run() {
        while (!isInterrupted() && isAlive()) {
            try {

                for (Holder holder : holders) {
                    holder.calculateNeed();
                }

                reAllocate();

                Thread.sleep(DEFAULT_SLEEP_TIME);

            } catch (Throwable e) {
                log.error(e);
            } finally {
                log.info("资源分配完毕，分配结果:");
                for (Holder holder : holders) {
                    log.info(holder);
                }
            }
        }
    }

}
