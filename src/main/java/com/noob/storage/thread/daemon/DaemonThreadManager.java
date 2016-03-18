package com.noob.storage.thread.daemon;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * 守护任务管理器
 * <p>所有继承自DaemonThread的子类在执行的时候都会注册到该管理器,
 * 可以利用该类进行一些信息管理与查看的功能.
 */
@Component
public class DaemonThreadManager {

    private static final Logger log = Logger.getLogger(DaemonThreadManager.class);

    /**
     * 任务队列
     */
    private final static List<DaemonThread> daemonTasks = new LinkedList<DaemonThread>();

    /**
     * 加载并启动所有任务
     */
    public static void startAllTask() {
        try {
            synchronized (daemonTasks) {
                daemonTasks.addAll(FilePersistentTaskLoader.loadAllTasks());
                for (DaemonThread task : daemonTasks) {
                    task.start();
                }
            }
            log.info("文件守护任务加载完毕!");
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        }
    }

    public static synchronized void register(DaemonThread task) {
        if (task != null
                && !daemonTasks.contains(task)
                && daemonTasks.add(task)) {
            log.info("守护任务[" + task.getTaskType() + "][" + task.getTaskName() + "]已添加到任务队列.");
        }
    }

    public static synchronized void removeTask(DaemonThread task) {
        if (task != null && daemonTasks.remove(task)) {
            log.info("守护任务[" + task.getTaskType() + "][" + task.getTaskName() + "]已从任务队列移除.");
        }
    }

}