package com.noob.storage.component.cache;

import com.noob.storage.component.cache.loader.LoadPolicy;
import com.noob.storage.component.cache.reject.RejectPolicy;

/**
 * 数据加载配置
 *
 * 注册中心不应该管理N个引擎,而是应该是管理多个配置
 * 不同的机制采用不同的配置
 *
 * @author luyun
 */
public class DataLoadConfig {

    //缓存key
    private String cacheKey;

    //缓存map中的key
    private String mapKey;

    //数据加载策略
    private LoadPolicy loadPolicy;

    //拒绝策略
    private RejectPolicy rejectPolicy;

    //数据类型
    private Class dataType;


}
