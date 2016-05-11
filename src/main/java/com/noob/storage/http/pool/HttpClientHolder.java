package com.noob.storage.http.pool;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.InitializingBean;

/**
 * httpClient持有者
 */
public abstract class HttpClientHolder implements InitializingBean {

    protected HttpClient httpClient;

    /**
     * 初始化httpClient
     */
    protected abstract void initHttpClient();

    /**
     * 获取httpClient实例
     */
    public HttpClient getHttpClient(){
        return httpClient;
    }

    public void afterPropertiesSet() throws Exception {

        initHttpClient();

        if (httpClient == null) {
            throw new RuntimeException("httpClient must not be null!");
        }
    }
}
