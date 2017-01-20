package com.noob.storage.component.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据加载任务
 *
 * @author luyun
 */
public class DataLoadTask<T> implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DataLoadTask.class);

    public CacheObject cacheObject;

    public DataLoadTask(CacheObject cacheObject) {
        this.cacheObject = cacheObject;
    }

    public void run() {
        try {
            loadDataFromSource(cacheObject);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    /*
     * 从实际的数据源加载数据
     *
     * @param cacheObject 加载参数
     */
    public T loadDataFromSource(CacheObject cacheObject) {

        return null;
    }

}
