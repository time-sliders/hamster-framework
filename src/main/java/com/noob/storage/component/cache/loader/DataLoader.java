package com.noob.storage.component.cache.loader;

import com.noob.storage.component.cache.CacheObject;
import com.noob.storage.component.cache.DataLoadTask;
import com.noob.storage.component.cache.policy.LoadPolicy;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * 数据加载器
 *
 * @author luyun
 */
public class DataLoader<T extends CacheObject> implements Serializable {

    private static final long serialVersionUID = 2091645502978039710L;

    //数据加载策略
    protected LoadPolicy loadPolicy;

    //数据类型
    protected Class dataType;

    //数据加载任务
    protected DataLoadTask<T> dataLoadTask;

    public T load(T t) {

        if (t == null
                || StringUtils.isBlank(t.getCacheKey())) {
            return null;
        }

        return loadPolicy.load(t, dataLoadTask);
    }

    public LoadPolicy getLoadPolicy() {
        return loadPolicy;
    }

    public Class getDataType() {
        return dataType;
    }

    public DataLoadTask getDataLoadTask() {
        return dataLoadTask;
    }

}
