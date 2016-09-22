package com.noob.storage.component.cache.config;

import com.noob.storage.component.cache.DataLoadTask;
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
    protected String key;

    //数据加载策略
    protected LoadPolicy loadPolicy;

    //拒绝策略
    protected RejectPolicy rejectPolicy;

    //数据类型
    protected Class dataType;

    //数据加载任务
    protected DataLoadTask dataLoadTask;

    public String getKey() {
        return key;
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
