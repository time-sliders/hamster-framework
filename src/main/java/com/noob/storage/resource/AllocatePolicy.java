package com.noob.storage.resource;

import java.util.Collection;

/**
 * 分配策略
 *
 * @param <T> allocate target
 * @param <O> the resource to be allocate
 * @author luyun
 * @since 2017.01.12
 */
public interface AllocatePolicy<O, T> {

    /**
     * 将一份资源按照某一个权重规则分配给一组对象
     *
     * @param resource   待分配的资源
     * @param targetList 需要被分配的对象组
     */
    void allocate(O resource, Collection<T> targetList);

}
