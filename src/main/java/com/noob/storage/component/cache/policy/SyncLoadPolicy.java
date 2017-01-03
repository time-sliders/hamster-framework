package com.noob.storage.component.cache.policy;

import com.noob.storage.component.cache.CacheObject;
import com.noob.storage.component.cache.DataLoadTask;

/**
 * 同步加载
 *
 * @author luyun
 */
public class SyncLoadPolicy implements LoadPolicy {

    @Override
    public <T extends CacheObject> T load(T t, DataLoadTask<T> task) {
        return task.loadDataFromSource(t);
    }
}
