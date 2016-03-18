package com.noob.storage.common.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigFactory {

    public static Map<String, FileConfig> configStore = new HashMap<String, FileConfig>();

    public static boolean register(FileConfig fileConfig) {
        try {
            configStore.put(fileConfig.getName(), fileConfig);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean register(String name, String configFilePath) {
        try {
            FileConfig fileConfig = new FileConfig(name, configFilePath);
            configStore.put(fileConfig.getName(), fileConfig);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public static String getConfig(String configName, String key) {
        FileConfig fileConfig = configStore.get(configName);
        return fileConfig != null ? fileConfig.getProperty(key) : null;
    }

    public static void saveAll() {
        for (String s : configStore.keySet()) {
            configStore.get(s).save();
        }
    }

    public static void save(String configName) {
        FileConfig config = configStore.get(configName);
        if (config != null) {
            config.save();
        }
    }

}
