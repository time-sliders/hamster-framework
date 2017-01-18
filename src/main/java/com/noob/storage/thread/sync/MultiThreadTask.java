package com.noob.storage.thread.sync;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 多线程协同任务<br/>
 * <p>
 * 为了提升响应速度等原因,系统可以将一次请求中多个互不相关的
 * 操作分发到多个线程一起去完成,如果为这些任务单独创建线程池
 * 感觉有一点资源浪费,且存在线程竞争。这里采用wait/notify
 * 模式搭建的一个多线程任务,可以达到想要的效果。
 * </p>
 * <p/>
 * 样例:
 * <pre>
 * ConcurrentMap context = new ConcurrentHashMap();
 * MultiThreadTask multiThreadTask = new MultiThreadTask(context);
 * multiThreadTask.addSubTask(new SubTask());
 * multiThreadTask.addSubTask(new SubTask());
 * multiThreadTask.start();
 * </pre>
 *
 * @author luyun
 * @see SubTask
 */
public class MultiThreadTask {

    private static final Logger logger = LoggerFactory.getLogger(MultiThreadTask.class);

    //需要完成的子任务总数
    protected int threadNum = 0;
    //主线程的等待锁
    private CountDownLatch subTaskLatch;
    //当前多线程任务是否已经启动(已经启动则不允许再添加子任务)
    protected AtomicBoolean isStarted = new AtomicBoolean(false);
    //子线程任务列表
    protected List<SubTask> subThreadTaskList;
    //可作为子线程共享内存,也可存储子线程参数
    protected ConcurrentMap<String, Object> context;
    //主线程最大等待时间(毫秒)
    private long maxWaitMillis;

    public MultiThreadTask(ConcurrentMap<String, Object> context, long maxWaitMillis) {
        this.context = context;
        this.maxWaitMillis = maxWaitMillis;
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
            threadNum++;
        }
    }

    /**
     * 启动这个多线程任务<br/>
     * block code
     */
    public void start() {

        try {
            if (!isStarted.compareAndSet(false, true)) return;
            subTaskLatch = new CountDownLatch(threadNum);

            // 启动子线程
            startAllSubTask();

            // 等待所有子线程处理完毕
            subTaskLatch.await(maxWaitMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warn(e.getMessage(), e);
        } finally {
            // 相关资源销毁
            destroy();
        }
    }

    /**
     * 销毁逻辑
     */
    protected void destroy() {
        // do nothing, just for subclass
    }

    /**
     * 启动所有子线程任务
     */
    protected void startAllSubTask() {

        // 没有指定子线程任务直接抛异常
        if (CollectionUtils.isEmpty(subThreadTaskList)) {
            throw new RuntimeException("no sub tasks!");
        }

        for (SubTask task : subThreadTaskList) {
            task.start();
        }
    }

    /**
     * 子线程任务完成时调用该方法<br/>
     * 如果所有子线程任务已全部完成,则结束主线程等待
     */
    void afterSubTaskFinish() {
        subTaskLatch.countDown();
    }

}
