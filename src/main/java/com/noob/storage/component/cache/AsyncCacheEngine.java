package com.noob.storage.component.cache;

import com.noob.storage.component.cache.loader.DataLoader;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luyun
 */
public abstract class AsyncCacheEngine implements InitializingBean {

    /**
     * 配置MAP,不同的Class根据不同的配置来查找数据
     */
    private Map<Class, DataLoader> configMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    private <T extends CacheObject> T get(T t) {

        String key = t.getCacheKey();

        if (StringUtils.isBlank(key)) {
            return null;
        }

        DataLoader<T> loader = configMap.get(t.getClass());
        if (loader == null) {
            throw new RuntimeException("异步缓存引擎无对应的配置,class:" + t.getClass());
        }

        return loader.load(t);
    }

    /**
     * 添加一个配置
     */
    public boolean register(DataLoader config) {
        return config != null
                && configMap.put(config.getDataType(), config) != null;
    }

    public void afterPropertiesSet() throws Exception {

    }

}
