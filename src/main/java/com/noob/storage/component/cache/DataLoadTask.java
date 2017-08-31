package com.noob.storage.component.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 数据加载任务
 *
 * @author luyun
 */
public class DataLoadTask<Q, R> implements Callable<R> {

    private static final Logger logger = LoggerFactory.getLogger(DataLoadTask.class);

    private AsyncCacheEngine<Q, R> engine;

    private Q param;

    DataLoadTask(AsyncCacheEngine<Q, R> engine, Q param) {
        this.engine = engine;
        this.param = param;
    }

    public R call() throws Exception {
        try {
            R result = engine.getDataFromSource(param);
            if (result != null) {
                engine.set(param, result);
            }
            return result;
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    void setEngine(AsyncCacheEngine<Q, R> engine) {
        this.engine = engine;
    }

    public Q getParam() {
        return param;
    }

    public void setParam(Q param) {
        this.param = param;
    }

}