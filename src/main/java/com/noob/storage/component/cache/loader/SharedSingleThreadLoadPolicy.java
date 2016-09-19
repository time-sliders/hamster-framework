package com.noob.storage.component.cache.loader;

import com.noob.storage.component.cache.DataLoadTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步共享单线程池加载策略<br/>
 * <p>数据量不大
 * <p>所有缓存数据都共用一个固定线程去加载数据
 *
 * @author luyun
 */
public class SharedSingleThreadLoadPolicy implements LoadPolicy {

    /**
     * 所有采用该策略的异步加载任务,共享同一个线程池
     */
    private static ExecutorService pool = Executors.newSingleThreadExecutor();

    private static SharedSingleThreadLoadPolicy instance = new SharedSingleThreadLoadPolicy();

    public static SharedSingleThreadLoadPolicy getInstance() {
        return instance;
    }

    private SharedSingleThreadLoadPolicy() {
    }

    @Override
    public void asyncLoadData(DataLoadTask loadTask) {
        pool.submit(loadTask);
    }

}
