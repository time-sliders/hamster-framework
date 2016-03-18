package com.noob.storage.thread.daemon;

import org.apache.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 守护线程任务
 */
public abstract class DaemonThread extends Thread {

    private static Logger log = Logger.getLogger(DaemonThread.class);

    /**
     * 守护任务的名称，落地的时候会根据改名成生成落地文件
     */
    protected String name;

    /**
     * 当前任务的类型，默认是类名字符串
     */
    protected String taskType;

    /**
     * 任务执行失败之后休眠多长时间执行
     */
    protected long intervalTime = 1000 * 60 * 30;

    /**
     * 任务当前的状态
     */
    private TaskStatus status;

    /**
     * 状态修改同步锁
     * <p>switchStatus() 与 markStop()需要保持同步
     */
    private ReentrantLock statusLock = new ReentrantLock();

    /**
     * 重试循环是否已经被终止
     */
    private boolean isLoopTerminal = false;

    public DaemonThread() {
        this.name = UUID.randomUUID().toString().replaceAll("-", "");
        this.taskType = this.getClass().getCanonicalName();
        this.status = TaskStatus.START_WAIT;
    }

    protected abstract boolean execute();

    @Override
    public void run() {
        before();
        while (!isLoopTerminal) {
            try {
                if (switchStatus(TaskStatus.RUNNING)
                        && execute()) {
                    afterSuccess();
                    isLoopTerminal = true;
                }
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
            } finally {
                if (switchStatus(TaskStatus.BLOCKING)) {
                    try {
                        Thread.sleep(intervalTime);
                    } catch (InterruptedException e) {
                        isLoopTerminal = true;
                    }
                }
            }
        }
        switchStatus(TaskStatus.TERMINAL);
        DaemonThreadManager.removeTask(this);
    }

    /**
     * 任务执行之前需要处理的事情
     */
    protected void before() {
        DaemonThreadManager.register(this);
    }

    /**
     * 任务<strong>[成功]</strong>执行之后需要处理的逻辑
     */
    protected void afterSuccess() {
    }

    /**
     * 状态切换
     * <li>切换到MARK_STOP 与 TERMINAL 可以直接切换
     * <li>如果任务已经结束或者已经被通知结束，则不允许再切换
     *
     * @param toStatus 需要切换到的状态
     */
    private synchronized boolean switchStatus(TaskStatus toStatus) {
        statusLock.lock();
        try {
            if (toStatus != TaskStatus.MARK_STOP && toStatus != TaskStatus.TERMINAL
                    && (status == TaskStatus.MARK_STOP || status == TaskStatus.TERMINAL)) {
                return false;
            }
            this.status = toStatus;
            return true;
        } finally {
            statusLock.unlock();
        }
    }

    /**
     * 将该任务标记为结束
     * <p>已经标记为结束的任务会在执行完毕之后结束，不再重试
     */
    public void markStop() {
        statusLock.lock();
        try {
            if (status == TaskStatus.TERMINAL
                    || status == TaskStatus.MARK_STOP) {
                return;
            }
            isLoopTerminal = true;
            if (status == TaskStatus.BLOCKING) {
                status = TaskStatus.MARK_STOP;
                this.interrupt();
            } else {
                status = TaskStatus.MARK_STOP;
            }
            log.info("任务[" + taskType + "][" + name + "]已被通知结束!");
        } finally {
            statusLock.unlock();
        }
    }

    public String getTaskName() {
        return name;
    }

    public String getTaskType() {
        return taskType;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setIntervalTime(long intervalTime) {
        if (intervalTime > 0) {
            this.intervalTime = intervalTime;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((taskType == null) ? 0 : taskType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DaemonThread other = (DaemonThread) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
