package com.noob.storage.io.multireader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 读取一行数据的任务<br/>
 * 从指定的reader中读取一行数据并处理,直到数据全部处理完
 *
 * @author luyun
 */
public abstract class ReaderTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ReaderTask.class);

    private ReaderEngine engine;

    private final BufferedReader reader;

    public ReaderTask(ReaderEngine engine, BufferedReader reader) {
        this.engine = engine;
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            String line;
            while (!engine.isFinish()
                    && (line = syncReadLine()) != null) {
                doBusiness(line);
            }
            engine.markFinish();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 从Reader中同步读取一行数据<br/>
     * 后续可以考虑将其优化成从缓冲队列中获取
     */
    private String syncReadLine() throws IOException {
        /*
         * 这里的synchronized 关键字放在syncReadLine()方法上并
         * 不能起到同步的作用。因为是通过多个ReaderTask来执行任务,
         * 对象之间互不影响,故需要在共享内存对象Reader上做同步
         */
        synchronized (reader) {
            return reader.readLine();
        }
    }

    /**
     * 处理一行数据(子类实现)
     */
    abstract boolean doBusiness(String line);

}
