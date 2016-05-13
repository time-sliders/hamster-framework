package com.noob.storage.component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheTemplate {



    public <T> T cache(CacheAble<T> cacheAble) {
        T data;

        data = cacheAble.getFromCache();

        if (data != null) {
            return data;
        }

        data = cacheAble.getFromSource();

        if (data != null) {
            cacheAble.cacheIt(data);
        }

        return data;
    }

    /**
     * 要被加入缓存逻辑的代码
     *
     * @param <T>
     */
    public interface CacheAble<T> {

        /**
         * 从缓存中查询
         */
        T getFromCache();


        /**
         * 从原始位置查询
         */
        T getFromSource();

        /**
         * 把查询结果加入缓存中
         */
        void cacheIt(T data);

    }

}
