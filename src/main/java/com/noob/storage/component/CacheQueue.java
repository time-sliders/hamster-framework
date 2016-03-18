package com.noob.storage.component;


import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 散列队列（缓存作用）
 * <li>Queue实现容量管理与FIFO原则,value值保存在Hash表实现快速查找
 * <li>容量满的时候添加元素，会移除最先添加进来的元素
 * <li>thread-safe
 */
public class CacheQueue<K, V> {

    private int capacity;

    private LinkedList<K> keyQueue;

    private Map<K, V> valueMap = new HashMap<K, V>();

    /**
     * 缓存队列
     *
     * @param capacity 队列容量
     */
    public CacheQueue(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity:" + capacity);
        }
        this.capacity = capacity;
        keyQueue = new LinkedList<K>();
    }

    public synchronized void put(K key, V value) {
        if (key != null && StringUtils.isNotBlank(key.toString()) && !contains(key)) {
            if (keyQueue.size() >= capacity) {
                valueMap.remove(keyQueue.poll());
            }
            keyQueue.add(key);
            valueMap.put(key, value);
        }
    }

    public V get(K key) {
        return valueMap.get(key);
    }

    public boolean contains(K key) {
        return valueMap.containsKey(key);
    }

}