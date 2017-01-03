package com.noob.storage.component.cache.policy;

import com.noob.storage.component.cache.CacheObject;
import com.noob.storage.component.cache.DataLoadTask;

/**
 * 数据加载策略
 *
 * @author luyun
 */
public interface LoadPolicy {

    <T extends CacheObject> T load(T t, DataLoadTask<T> task);

}
