package com.noob.storage.component.cache.reject;

import com.noob.storage.component.cache.AsyncCacheEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author luyun
 * @since app6.1
 */
public class DefaultRejectPolicy<Q> implements RejectPolicy<Q> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRejectPolicy.class);

    @Override
    public void reject(Q param, AsyncCacheEngine engine) {
        logger.warn("AsyncCacheEngine [{}] currentKeySet is full! param:{}",
                new Object[]{engine, param});
    }
}
