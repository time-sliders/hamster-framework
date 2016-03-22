package com.noob.storage.thread.daemon;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 守护线程任务
 */
public abstract class DaemonThread extends Thread {

    /**
     * 守护任务的名称,唯一
     */
    protected String name;

    /**
     * 当前任务的类型，默认是类名字符串
     */
    protected String taskType;

    /**
     * 失败休眠时间
     */
    protected long intervalTime = 1000 * 60 * 30;

    /**
     * 任务终止标志
     * <p>不同于线程中断</p>
     */
    private AtomicBoolean terminated = new AtomicBoolean(false);

    public DaemonThread() {
        this.name = UUID.randomUUID().toString().replaceAll("-", "");
        this.taskType = this.getClass().getCanonicalName();
    }

    /**
     * 子类具体业务逻辑
     * <strong>务必保持幂等</strong>
     */
    protected abstract boolean execute();

    @Override
    public void run() {

        open();

        before();

        while (!isTerminated() && !isInterrupted()) {

            if (execute()) {

                afterSuccess();

                close();

                break;
            }

            //失败休眠
            try {
                Thread.sleep(intervalTime);
            } catch (InterruptedException e) {
                close();
            }
        }
    }

    /**
     * 任务执行之前需要处理的事情
     */
    protected void before() {
    }

    /**
     * 任务<strong>[成功]</strong>执行之后需要处理的逻辑
     */
    protected void afterSuccess() {
    }


    /**
     * 判断是否继续执行任务
     */
    public boolean isTerminated() {
        return this.terminated.get();
    }

    /**
     * 停止任务操作
     */
    public void close() {
        this.terminated.set(true);
    }

    /**
     * 打开任务操作
     */
    public void open() {
        this.terminated.set(false);
    }


    public String getTaskName() {
        return name;
    }

    public String getTaskType() {
        return taskType;
    }

    @Override
    public String toString() {
        return name;
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
