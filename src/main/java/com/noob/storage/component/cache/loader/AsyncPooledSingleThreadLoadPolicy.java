package com.noob.storage.component.cache.loader;

import com.noob.storage.component.cache.AsyncCacheEngine;
import com.noob.storage.component.cache.DataLoadTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步单线程池加载策略<br/>
 * <p>数据量不大
 * <p>所有缓存数据都共用一个固定线程去加载数据
 *
 * @author luyun
 */
public class AsyncPooledSingleThreadLoadPolicy<Q, T> implements LoadPolicy<Q, T> {

    private ExecutorService pool = Executors.newSingleThreadExecutor();

    @Override
    public void asyncLoadData(Q q, AsyncCacheEngine<Q, T> engine) {
        pool.submit(new DataLoadTask<Q, T>(engine, q));
    }

}
