package com.noob.storage.component.cache.loader;

import com.noob.storage.component.cache.DataLoadTask;

/**
 * 异步新线程数据加载策略<br/>
 * <p>适用与只有一个缓存数据的情况
 * <p>不采用线程池,而是直接new一个新的线程去查询数据
 *
 * @author luyun
 */
public class AsyncThreadLoadPolicy implements LoadPolicy{

    private static LoadPolicy instance = new AsyncThreadLoadPolicy();

    public static LoadPolicy getInstance(){
        return instance;
    }

    private AsyncThreadLoadPolicy() {
    }

    public void asyncLoadData(DataLoadTask loadTask) {
        loadTask.start();
    }

}
