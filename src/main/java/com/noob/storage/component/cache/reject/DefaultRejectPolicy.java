package com.noob.storage.component.cache.reject;

import com.noob.storage.component.cache.AsyncCacheEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author luyun
 * @since app6.1
 */
public class DefaultRejectPolicy implements RejectPolicy {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRejectPolicy.class);


    private static RejectPolicy instance = new DefaultRejectPolicy();

    public static RejectPolicy getInstance() {
        return instance;
    }

    private DefaultRejectPolicy() {
    }

    @Override
    public void reject(Object param, AsyncCacheEngine engine) {
        logger.warn("AsyncCacheEngine [{}] currentKeySet is full! param:{}",
                new Object[]{engine, param});
    }
}
