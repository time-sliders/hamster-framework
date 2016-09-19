package com.noob.storage.component.cache;

import com.noob.storage.component.cache.loader.LoadPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 数据加载任务
 *
 * @author luyun
 */
public class DataLoadTask extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(DataLoadTask.class);

    private AsyncCacheEngine engine;

    private LoadPolicy loadPolicy;

    private Object param;

    public DataLoadTask(AsyncCacheEngine engine, Object param, LoadPolicy loadPolicy) {
        this.engine = engine;
        this.param = param;
        this.loadPolicy = loadPolicy;
    }

    public void load() {
        loadPolicy.asyncLoadData(this);
    }

    public void run() {
        try {
            Object result = engine.getDataFromSource(param);
            engine.set(param, result);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        } finally {
            engine.getCurrentKeySet().remove(engine.getMapKey(param));
        }
    }

    void setEngine(AsyncCacheEngine engine) {
        this.engine = engine;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

}
