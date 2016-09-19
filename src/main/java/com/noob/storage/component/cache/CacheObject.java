package com.noob.storage.component.cache;

/**
 * 缓存对象
 *
 * @author luyun
 */
public interface CacheObject {

    /**
     * 获取该对象在缓存中的key
     */
    String getCacheKey();

}
