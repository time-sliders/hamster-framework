package com.noob.storage.thread.sync;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 多线程协同任务<br/>
 * <p>
 * 为了提升响应速度等原因,系统可以将一次请求中多个互不相关的
 * 操作分发到多个线程一起去完成,如果为这些任务单独创建线程池
 * 感觉有一点资源浪费,且存在线程竞争。这里采用wait/notify
 * 模式搭建的一个多线程任务,可以达到想要的效果。
 * </p>
 *
 * @author luyun
 * @see SubTask
 * @since app6.1
 */
public final class MultiThreadTask {

    private static final Logger logger = LoggerFactory.getLogger(MultiThreadTask.class);

    //已完成的子任务总数
    private int finishedTaskCount = 0;
    //需要完成的子任务总数
    private int taskCount = 0;
    //当前多线程任务是否已经启动(已经启动则不允许再添加子任务)
    private AtomicBoolean isStarted = new AtomicBoolean(false);
    //子线程任务列表
    private List<SubTask> subThreadTaskList;
    //可作为子线程共享内存,也可存储子线程参数
    private ConcurrentMap<String, Object> context;

    public MultiThreadTask(ConcurrentMap<String, Object> context) {
        this.context = context;
    }

    /**
     * 添加一个子线程任务
     */
    public void addSubTask(SubTask task) {

        if (task == null) {
            throw new NullPointerException("task must not be null!");
        }

        if (isStarted.get()) {
            throw new IllegalStateException("already started");
        }

        if (subThreadTaskList == null) {
            subThreadTaskList = new ArrayList<SubTask>();
        }

        task.setMainTask(this);
        if (subThreadTaskList.add(task)) {
            taskCount++;
        }
    }

    /**
     * 启动这个多线程任务
     */
    public void start() {

        if (!isStarted.compareAndSet(false, true)) return;

        // 启动子线程
        startAllSubTask();

        // 主线程等待
        waitSubTaskFinish();
    }

    /**
     * 启动所有子线程任务
     */
    private void startAllSubTask() {

        // 没有指定子线程任务直接抛异常
        if (CollectionUtils.isEmpty(subThreadTaskList)) {
            throw new RuntimeException("no sub tasks!");
        }

        for (SubTask task : subThreadTaskList) {
            task.start();
        }
    }

    /**
     * 在主线程启动完所有子线程任务之后,主线程进入等待(wait)
     * 直到所有子线程任务完成,才唤醒(notify)主线程。
     *
     * @see #afterSubTaskFinish()
     */
    private void waitSubTaskFinish() {
        synchronized (this) {
            /**
             * 先加锁,再判断
             * 这个判断是为了防止子线程过快的执行完,主线程还没有进入
             * 到等待状态时,避免主线程等待
             */
            if (!isAllTaskFinished()) {
                try {
                    /**
                     * 等待(不要去掉wait的参数,这是对异
                     * 常情况的一个容错,防止主线程死锁)
                     */
                    this.wait(1000 * 10);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }

        }
    }

    /**
     * 子线程任务完成时调用该方法<br/>
     * 如果所有子线程任务已全部完成,则结束主线程等待
     *
     * @see #waitSubTaskFinish()
     */
    void afterSubTaskFinish() {
        synchronized (this) {
            if (++finishedTaskCount >= taskCount) {
                this.notify();
            }
        }
    }

    ConcurrentMap<String, Object> getContext() {
        return context;
    }

    private boolean isAllTaskFinished() {
        return finishedTaskCount >= taskCount;
    }

}
