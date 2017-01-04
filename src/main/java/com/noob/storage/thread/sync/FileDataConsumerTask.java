package com.noob.storage.thread.sync;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;

/**
 * @author luyun
 */
public abstract class FileDataConsumerTask extends SubTask<MultiThreadFileReaderTask> {

    private static final Logger logger = LoggerFactory.getLogger(FileDataConsumerTask.class);

    /**
     * 当前任务的模式:{@link Mode}
     */
    private volatile int mode = Mode.Execute;

    public void doBusiness(ConcurrentMap<String, Object> context) {

        while (!mainTask.isAllDataRead || CollectionUtils.isNotEmpty(mainTask.lineDataBuffer)) {

            if (Mode.Read == mode) {
                //当为读取模式时,线程负责从文件中读取数据到共享缓存队列中
                mainTask.read();
            } else if (Mode.Execute == mode) {
                //当为执行模式时,线程负责从共享缓存队列中取数据并进行处理
                execute(context);
            }

            //尝试模式切换
            modeSwitch();
        }
    }

    /**
     * 执行模式处理数据
     */
    private void execute(ConcurrentMap<String, Object> context) {

        String lineData;//文件中的一行数据

        while (mainTask.lineDataBuffer.size() > 0) {

            lineData = mainTask.take();

            if (StringUtils.isBlank(lineData)) {
                continue;
            }

            try {
                processLineData(lineData, context);
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * 处理一行数据
     *
     * @param lineData 文件中的一行数据
     * @param context  多线程上下文
     */
    public abstract void processLineData(String lineData, ConcurrentMap<String, Object> context);

    /**
     * 模式切换，将任务模式切换到另一种状态
     */
    boolean modeSwitch() {

        if (this.mode == Mode.Execute
                && mainTask.isHasReadThread.compareAndSet(false, true)) {
            // 执行模式-->读取模式
            this.mode = Mode.Read;

        } else if (this.mode == Mode.Read) {
            // 读取模式-->执行模式
            this.mode = Mode.Execute;
        }

        return true;
    }

    /**
     * 子任务模式
     */
    interface Mode {
        /**
         * 读取模式
         */
        int Read = 0;
        /**
         * 处理模式
         */
        int Execute = 1;
    }

}
