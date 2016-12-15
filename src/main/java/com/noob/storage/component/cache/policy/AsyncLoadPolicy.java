package com.noob.storage.component.cache.policy;

import com.noob.storage.component.cache.CacheObject;
import com.noob.storage.component.cache.DataLoadTask;

/**
 * 异步加载策略<br/>
 *
 * <li>数据始终保存在缓存</li>
 * <li>当达到超时时间之后,依然以[旧数据]响应,但会尝试重新从数据源查询数据,只有当获取到新数据之后,才替换旧数据</li>
 *
 * @author luyun
 */
public class AsyncLoadPolicy implements LoadPolicy {

    @Override
    public <T extends CacheObject> T load(T t, DataLoadTask<T> task) {
        return null;
    }

}
