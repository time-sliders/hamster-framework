package com.noob.storage.thread.daemon;

public enum TaskStatus {

    START_WAIT(1, "启动等待"),
    RUNNING(1 >> 1, "正在运行"),
    BLOCKING(1 >> 2, "休眠阻塞"),
    MARK_STOP(1 >> 3, "标记结束"),
    TERMINAL(1 >> 4, "结束");

    /**
     * 任务ID
     */
    int id;

    /**
     * 任务名称
     */
    String name;

    TaskStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
