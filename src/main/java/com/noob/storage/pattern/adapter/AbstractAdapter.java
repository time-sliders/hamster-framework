package com.noob.storage.pattern.adapter;

import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象适配器
 *
 * @author luyun
 */
public class AbstractAdapter<K/*适配器路由参数类型*/, E/*适配器返回类型*/> {

    // 映射Map
    Map<K, E> executorMapping = new HashMap<K, E>();

    // 默认的执行者
    E defaultExecutor;

    // 注册默认的执行者
    void registerDefaultExecutor(E defaultExecutor) {
        this.defaultExecutor = defaultExecutor;
    }

    // 注册一个执行者
    public void register(K k, E e) {
        executorMapping.put(k, e);
    }

    /**
     * 根据不同的字段类型,获取不同的执行者
     */
    public E getExecutor(K k) {

        E executor;

        if (k == null) {
            executor = defaultExecutor;
        } else {
            executor = executorMapping.get(k);
            if (executor == null) {
                executor = defaultExecutor;
            }
        }

        return executor;
    }

    public void afterPropertiesSet() throws Exception {
        if (defaultExecutor == null && MapUtils.isEmpty(executorMapping)) {
            throw new RuntimeException(getClass() + "尚未注册执行者");
        }
    }

}
