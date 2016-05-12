package com.noob.storage.common.config;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件配置管理器<br/>
 * 建议通过Spring注册为组件,通过构造器参数 指定需要加载的配置文件.
 */
public class FileConfigManager {

    public Map<String/*name*/, FileConfig> configStore = new HashMap<String, FileConfig>();

    public FileConfigManager(Map<String/*name*/, String/*location*/> initConfig) {

        if (MapUtils.isEmpty(initConfig)) {
            return;
        }

        for (String name : initConfig.keySet()) {
            String location = initConfig.get(name);
            if (StringUtils.isBlank(name) && StringUtils.isBlank(location)) {
                continue;
            }
            configStore.put(name, new FileConfig(name, location));
        }

    }

    public boolean register(FileConfig fileConfig) {
        try {
            configStore.put(fileConfig.getName(), fileConfig);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public boolean register(String name, String location) {
        return register(new FileConfig(name, location));
    }

    public String getConfig(String configName, String key) {
        FileConfig fileConfig = configStore.get(configName);
        return fileConfig != null ? fileConfig.getProperty(key) : null;
    }

}
