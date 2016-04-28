package com.noob.storage.http;

import com.alibaba.dubbo.common.utils.Assert;
import com.noob.storage.http.base.HttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Http连接支持<br/>
 * Spring 配置文件中初始化HttpClientBuilder 作为httpClient注入
 *
 * @author luyun
 * @since 2016.04.28
 */
public class HttpSupport implements InitializingBean {

    @Autowired
    private HttpClient httpClient;


    void checkConfig(){
        Assert.notNull(httpClient, "httpClient must not be null!");
    }

    public void afterPropertiesSet() throws Exception {
        checkConfig();
    }
}
