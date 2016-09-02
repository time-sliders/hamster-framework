package com.noob.storage.file;

import com.noob.storage.thread.sync.SubTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;

/**
 * 文件中的一行数据的处理任务<br/>
 *
 * @author luyun
 * @see MultiThreadFileReaderTask
 */
public abstract class FileDataProcessTask extends SubTask {

    private static final Logger logger = LoggerFactory.getLogger(FileDataProcessTask.class);

    private MultiThreadFileReaderTask reader;

    public void doBusiness(ConcurrentMap<String, Object> context) {

        String lineData;//文件中的一行数据

        while ((lineData = reader.take()) != null) {
            try {
                processLineData(lineData, context);
            } catch (Exception e) {
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
    public abstract void processLineData(String lineData, ConcurrentMap<String, Object> context);

    void setReader(MultiThreadFileReaderTask reader) {
        this.reader = reader;
    }
}
