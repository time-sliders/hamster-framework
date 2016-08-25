package com.noob.storage.io.multireader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 一个采用多个线程按照每一行数据,同时处理同一个文件的任务<br/>
 * <p>多个线程共享同一个文件的BufferedReader对象。将readLine()方法
 * 设置为同步代码,即可达到效果</p>
 *
 * @author luyun
 */
public class ReaderEngine<T extends ReaderTask> {

    private static final Logger logger = LoggerFactory.getLogger(ReaderEngine.class);

    // 是否以及启动执行
    private AtomicBoolean isExecuting = new AtomicBoolean(false);

    // 文件中是否还有其他数据
    private AtomicBoolean isHaveNextLine = new AtomicBoolean(true);

    public void execute(File file) {
        BufferedReader reader = null;
        isExecuting.compareAndSet(false, true);
        try {
            reader = new BufferedReader(new FileReader(file));

        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        } finally {
            isExecuting.compareAndSet(true, false);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * 当前任务是否已经结束
     */
    boolean isFinish() {
        return isHaveNextLine.get();
    }

    /**
     * 将当前任务标记为结束
     */
    void markFinish() {
        isHaveNextLine.compareAndSet(true, false);
    }
}
