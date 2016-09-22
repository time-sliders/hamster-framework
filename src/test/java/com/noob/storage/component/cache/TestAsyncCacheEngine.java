package com.noob.storage.component.cache;

import com.noob.storage.component.cache.config.SingleCacheConfig;
import com.noob.storage.http.base.Property;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author luyun
 * @since app6.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-context.xml")
public class TestAsyncCacheEngine {

    @Autowired
    private CacheEngineRegistry cacheEngineRegistry;

    public void testE() throws Exception {
        cacheEngineRegistry.register(Property.class, new SingleCacheConfig("TEST_PROPERTY_KEY", Property.class, null));
    }

}
