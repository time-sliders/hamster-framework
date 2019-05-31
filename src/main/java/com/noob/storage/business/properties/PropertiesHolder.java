package com.noob.storage.business.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 卢云(luyun)
 * @version 1.0
 * @since 2019.05.30
 */
@Component
public class PropertiesHolder {

    @Value("${jdbc.druid.url}")
    private String dbUrl;

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }
}
