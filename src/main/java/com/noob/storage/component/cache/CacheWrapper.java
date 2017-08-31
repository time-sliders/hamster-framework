package com.noob.storage.component.cache;

import java.io.Serializable;

/**
 * 缓存数据包装类
 *
 * @author luyun
 */
public class CacheWrapper<T> implements Serializable {

    private static final long serialVersionUID = -565679254618267452L;

    //缓存数据插入时间
    private long insertTime;

    //缓存数据超时时间
    private long timeout;

    private T data;

    /**
     * 默认的构造方法,不要删除,反射生成对象的时候需要用到
     */
    public CacheWrapper() {
    }

    /**
     * @param data    缓存的数据  <=0 时表示永远超时()
     * @param timeout 超时时间 毫秒
     */
    public CacheWrapper(T data, long timeout) {
        this.data = data;
        this.timeout = timeout;
        insertTime = System.currentTimeMillis();
    }

    /**
     * 判断当前缓存对象是否已经超时
     * bean里面非属性方法尽量不要以is开头,会在redis多存一个没有必要的boolean值
     */
    public boolean alreadyTimeout() {
        return timeout <= 0L || insertTime + timeout < System.currentTimeMillis();
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(long insertTime) {
        this.insertTime = insertTime;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
