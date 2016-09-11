package com.noob.storage.component.cache.reject;

import com.noob.storage.component.cache.AsyncCacheEngine;

/**
 * 拒绝策略
 *
 * @author luyun
 */
public interface RejectPolicy<Q> {

    void reject(Q param, AsyncCacheEngine engine);
}
