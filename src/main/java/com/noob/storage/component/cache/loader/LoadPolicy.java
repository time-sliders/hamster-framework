package com.noob.storage.component.cache.loader;

import com.noob.storage.component.cache.AsyncCacheEngine;

/**
 * 数据加载策略
 *
 * @author luyun
 */
public interface LoadPolicy<Q, T> {

    void asyncLoadData(Q q, AsyncCacheEngine<Q, T> engine);

}
