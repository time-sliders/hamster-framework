package com.noob.storage.component.cache;

import com.noob.storage.pattern.adapter.AbstractAdapter;
import org.springframework.stereotype.Component;

/**
 * 引擎管理器
 *
 * @author luyun
 */
@Component
public class CacheEngineRegistry extends AbstractAdapter<String,AsyncCacheEngine>{

    public void registry(Class cacheClass,AsyncCacheEngine engine){

    }

}
