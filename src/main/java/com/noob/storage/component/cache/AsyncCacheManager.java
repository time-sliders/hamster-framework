package com.noob.storage.component.cache;

import com.noob.storage.pattern.adapter.AbstractAdapter;

import java.util.concurrent.ConcurrentMap;

/**
 * 异步缓存管理器
 *
 * @author luyun
 * @since app6.1
 */
public class AsyncCacheManager extends AbstractAdapter<String, DataLoadTask> {

    private ConcurrentMap<String,Class> context;

    public <T> T get(Object obj, Class<T> clazz) {
        return null;
    }

}
