package com.noob.storage.thread.schedule;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Scheduler implements Runnable {

    private Logger log = Logger.getLogger(getClass());

    private int maxnum;

    private List<Holder> holders = Collections.synchronizedList(new ArrayList<Holder>());

    /**
     * 分配策略
     * 在调度者注册或者释放资源持有对象的时候，根据该策略来重新分配任务
     * 调度器启动之后也会每隔一段时间自动重新分配
     */
    private ResourceAllocatePolicy alocatePolicy;

    private static final ResourceAllocatePolicy defaultAllocatePolicy
            = new ProportionAllocatePolicy();

    public Scheduler(int maxnum) {
        this(maxnum, defaultAllocatePolicy);
    }

    public Scheduler(int maxnum, ResourceAllocatePolicy alocatePolicy) {
        this.maxnum = maxnum;
        this.alocatePolicy = alocatePolicy;
    }

    /**
     * 根据指定的分配策略重新分配资源
     */
    private synchronized void reAllocate() {
        alocatePolicy.allocate(maxnum, holders);
    }

    /**
     * 注册一个资源持有者并分配资源
     */
    public void register(Holder holder) {
        if (holder == null) {
            throw new NullPointerException();
        }
        if (holder.getNeedForReSource() <= 0) {
            holder.setNeedForReSource(getAvgNeed());
        }
        if (holders.add(holder)) {
            holder.setScheduler(this);
            reAllocate();
        }
    }

    /**
     * 获取已注册持有者的平均资源需求度
     */
    public int getAvgNeed() {
        synchronized (holders) {
            int countNeed = 0;
            for (int i = 0; i < holders.size(); i++) {
                countNeed += holders.get(i).getNeedForReSource();
            }
            return countNeed / holders.size();
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
     * 获取当前调度器空闲的资源量
     */
    public int idleNum() {
        synchronized (holders) {
            int usedNum = 0;
            for (Iterator<Holder> i = holders.iterator(); i.hasNext(); ) {
                usedNum += i.next().getHoldNum();
            }
            return maxnum - usedNum;
        }
    }

    /**
     * 调度器每隔一段时间动态分配资源
     */
    public void run() {
        while (true) {
            try {
                for (Holder holder : holders) {
                    holder.calculateNeed();
                }
                reAllocate();
                Thread.sleep(1000 * 60);
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
