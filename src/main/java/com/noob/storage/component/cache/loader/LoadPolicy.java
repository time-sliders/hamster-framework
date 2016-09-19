package com.noob.storage.component.cache.loader;

import com.noob.storage.component.cache.DataLoadTask;

/**
 * 数据加载策略
 *
 * @author luyun
 */
public interface LoadPolicy {

    void asyncLoadData(DataLoadTask loadTask);

}
