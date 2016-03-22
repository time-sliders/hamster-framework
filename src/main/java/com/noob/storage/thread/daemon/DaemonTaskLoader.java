package com.noob.storage.thread.daemon;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

/**
 * 守护任务管理器
 */
@Component
public class DaemonTaskLoader {

    private final Logger log = Logger.getLogger(DaemonTaskLoader.class);

    /**
     * 任务队列
     */
    private final List<DaemonThread> daemonTasks = new LinkedList<DaemonThread>();

    /**
     * 加载并启动所有任务
     */
    @PostConstruct
    public void load() {
        try {
            List<DaemonThread> tasks = FilePersistentTaskLoader.loadAllTasks();

            if (CollectionUtils.isEmpty(tasks)) {
                return;
            }
            daemonTasks.addAll(tasks);

            for (DaemonThread task : daemonTasks) {
                task.start();
            }
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        } finally {
            log.info("文件守护任务加载完毕!");
        }
    }

    public synchronized boolean register(DaemonThread task) {
        return task != null && !daemonTasks.contains(task) && daemonTasks.add(task);
    }

    public synchronized void removeTask(DaemonThread task) {
        if (task != null && daemonTasks.remove(task)) {
            log.info("守护任务[" + task.getTaskType() + "][" + task.getTaskName() + "]已从任务队列移除.");
        }
    }

}