package com.noob.storage.component.cache.loader;

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
public class SingleThreadLoadPolicy implements LoadPolicy {

    /**
     * 每一个加载实例使用一个线程池
     */
    private ExecutorService pool = Executors.newSingleThreadExecutor();

    @Override
    public void asyncLoadData(DataLoadTask loadTask) {
        pool.submit(loadTask);
    }

}
