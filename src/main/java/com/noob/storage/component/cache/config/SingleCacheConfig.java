package com.noob.storage.component.cache.config;

import com.noob.storage.component.cache.DataLoadTask;
import com.noob.storage.component.cache.loader.AsyncThreadLoadPolicy;
import com.noob.storage.component.cache.reject.DefaultRejectPolicy;

/**
 * 单缓存配置<br/>
 * <p>
 * 缓存钟只有一个对象,但缓存配置时,系统不会使用线程池,
 * 而是每次异步加载数据时,直接使用新的线程去获取数据
 * </p>
 *
 * @author luyun
 */
public class SingleCacheConfig extends DataLoadConfig {

    public SingleCacheConfig(String key, Class dataType, DataLoadTask dataLoadTask) {
        this.key = key;
        this.dataType = dataType;
        this.dataLoadTask = dataLoadTask;
        this.loadPolicy = AsyncThreadLoadPolicy.getInstance();
        this.rejectPolicy = DefaultRejectPolicy.getInstance();
    }


}
