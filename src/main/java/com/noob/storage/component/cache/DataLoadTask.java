package com.noob.storage.component.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 数据加载任务
 *
 * @author luyun
 */
public class DataLoadTask<Q, R> extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(DataLoadTask.class);

    private AsyncCacheEngine<Q, R> engine;

    private Q param;

    public DataLoadTask(AsyncCacheEngine<Q, R> engine, Q param) {
        this.engine = engine;
        this.param = param;
    }

    public void run()  {
        try {
            R result = engine.getDataFromSource(param);
            engine.set(param, result);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        } finally {
            engine.getCurrentKeySet().remove(engine.getMapKey(param));
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
