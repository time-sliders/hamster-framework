package com.noob.storage.thread.ob;

/**
 * 任务未完成异常
 * <p>线程池的观察者模式中，当观察者将所有任务放置到线程池并启动之后，观察者进入等待状态，
 * 如果等待超时任务仍然没有结束，则会抛出该异常
 */
public class TaskNotFinishException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 2703637515491415896L;

    public TaskNotFinishException() {
    }

    public TaskNotFinishException(String message) {
        super(message);
    }

}
