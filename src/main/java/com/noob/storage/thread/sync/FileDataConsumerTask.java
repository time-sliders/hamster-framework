package com.noob.storage.thread.sync;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author luyun
 */
public abstract class FileDataConsumerTask<C> extends SubTask<C> {

    private static final Logger logger = LoggerFactory.getLogger(FileDataConsumerTask.class);

    /**
     * 当前任务的模式:{@link Mode}
     */
    volatile int mode = Mode.EXE;

    private MultiThreadFileSchedulerTask<C> scheduler;

    public void doBusiness(C context) {
        while (true) {
            switch (mode) {
                case Mode.READ:
                    if (!scheduler.isAllDataRead.get()) {
                        scheduler.read();
                    }
                    break;

                case Mode.EXE:
                    if (CollectionUtils.isNotEmpty(scheduler.dataBuffer)) {
                        execute(context);
                    } else if (scheduler.hasReadThread.get()) {
                        /*
                         * 为了避免当读取线程因为某些原因导致读取速度过慢的时候
                         * 消费线程会使CPU满负荷运转的问题调整，这里消费线程如
                         * 果发现队列没有数据且已经存在读取线程时，进入await等
                         * 待读取线程读取数据
                         */
                        scheduler.lock.lock();
                        try {
                            if (!scheduler.isAllDataRead.get()) {// 必须在lock锁内部做判断
                                scheduler.consumeCondition.await();
                            }
                        } catch (InterruptedException e) {
                            logger.error(e.getMessage(), e);
                        } finally {
                            scheduler.lock.unlock();
                        }
                    }
                    break;
            }

            if (scheduler.isAllDataRead.get() && scheduler.dataBuffer.size() <= 0) {
                break;
            } else {
                /*
                 * 尝试模式轮换
                 */
                tryModeSwitch();
            }
        }
    }

    /**
     * 执行模式处理数据
     */
    private void execute(C context) {
        String data;
        while ((data = scheduler.dataBuffer.poll()) != null) {
            if (StringUtils.isBlank(data)) {
                continue;
            }
            try {
                processData(data, context);
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 处理一行数据
     *
     * @param lineData 文件中的一行数据
     * @param context  多线程上下文
     */
    public abstract void processData(String lineData, C context) throws InterruptedException;

    /**
     * 读写轮换
     */
    private void tryModeSwitch() {
        switch (mode) {
            case Mode.EXE:
                if (scheduler.hasReadThread.compareAndSet(false, true)) {
                    this.mode = Mode.READ;
                }
                break;
            case Mode.READ:
                if (scheduler.hasReadThread.compareAndSet(true, false)) {
                    this.mode = Mode.EXE;
                }
                break;
        }
    }

    @Override
    void setMainTask(MultiThreadTask<C> mainTask) {
        super.setMainTask(mainTask);
        scheduler = (MultiThreadFileSchedulerTask<C>) mainTask;
    }

    /**
     * 任务模式
     */
    interface Mode {
        /**
         * 读取模式
         */
        int READ = 0;
        /**
         * 处理模式
         */
        int EXE = 1;
    }

}
