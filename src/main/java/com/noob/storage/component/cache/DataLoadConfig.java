package com.noob.storage.component.cache;

import com.noob.storage.component.cache.loader.AsyncThreadLoadPolicy;
import com.noob.storage.component.cache.loader.LoadPolicy;
import com.noob.storage.component.cache.reject.DefaultRejectPolicy;
import com.noob.storage.component.cache.reject.RejectPolicy;

import java.io.Serializable;

/**
 * 数据加载配置
 * <p>
 * 注册中心不应该管理N个引擎,而是应该是管理多个配置
 * 不同的机制采用不同的配置
 *
 * @author luyun
 */
public class DataLoadConfig implements Serializable{

    private static final long serialVersionUID = 2091645502978039710L;

    //缓存key
    private String key;

    //缓存map中的key
    private String mapKey;

    //数据加载策略
    private LoadPolicy loadPolicy;

    //拒绝策略
    private RejectPolicy rejectPolicy;

    //数据类型
    private Class dataType;

    //数据加载任务
    private DataLoadTask dataLoadTask;

    public DataLoadConfig(String key, String mapKey, Class dataType) {
        this(key, mapKey, AsyncThreadLoadPolicy.getInstance(),
                DefaultRejectPolicy.getInstance(), dataType);
    }

    public DataLoadConfig(String key, String mapKey, LoadPolicy loadPolicy,
                          RejectPolicy rejectPolicy, Class dataType) {
        this.key = key;
        this.mapKey = mapKey;
        this.loadPolicy = loadPolicy;
        this.rejectPolicy = rejectPolicy;
        this.dataType = dataType;
    }

    public String getKey() {
        return key;
    }

    public String getMapKey() {
        return mapKey;
    }

    public LoadPolicy getLoadPolicy() {
        return loadPolicy;
    }

    public RejectPolicy getRejectPolicy() {
        return rejectPolicy;
    }

    public Class getDataType() {
        return dataType;
    }

    public DataLoadTask getDataLoadTask() {
        return dataLoadTask;
    }

    public boolean selfCheck() {
        //TODO
        return true;
    }
}
