package com.noob.storage.component.cache;

import org.springframework.stereotype.Component;

/**
 * @author luyun
 * @since app6.1
 */
@Component
public class TestEngine extends AsyncCacheEngine<String,String>{

    @Override
    protected int getMaxWaitSize() {
        return 1000;
    }

    @Override
    protected int getPoolSize() {
        return 1000;
    }

    @Override
    protected String getDataFromSource(String param) throws Exception {
        return "zhwwashere";
    }

    @Override
    protected String getMapKey(String param) {
        return "test-engine-map";
    }

    @Override
    protected String getKey() {
        return "test-engine-key";
    }
}