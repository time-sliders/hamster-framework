package com.noob.storage.component.cache;

import com.noob.storage.component.cache.config.DataLoadConfig;
import com.noob.storage.http.base.Property;
import com.noob.storage.pattern.adapter.AbstractAdapter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 引擎管理器
 *
 * @author luyun
 */
@Component
public class CacheEngineRegistry extends AbstractAdapter<Class, DataLoadConfig> implements InitializingBean {

}
