package com.noob.storage.component.cache.loader;

import com.noob.storage.component.cache.AsyncCacheEngine;
import com.noob.storage.component.cache.DataLoadTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异步新线程数据加载策略<br/>
 * <p>适用与只有一个缓存数据的情况
 * <p>不采用线程池,而是直接new一个新的线程去查询数据
 *
 * @author luyun
 */
public class AsyncThreadLoadPolicy<Q, T> {

    public void asyncLoadData(Q q, AsyncCacheEngine<Q, T> engine) {
        new DataLoadTask<Q, T>(engine, q).start();
    }

}
