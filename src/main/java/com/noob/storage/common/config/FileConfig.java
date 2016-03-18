package com.noob.storage.common.config;

import com.noob.storage.utils.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class FileConfig {

    private static Logger logger = Logger.getLogger(FileConfig.class);

    /**
     * 配置文件存放的路径
     */
    private String configFilePath = null;

    private String name = null;

    /**
     * 读取到的配置
     */
    private Properties properties;

    public FileConfig(String name, String configFilePath) {
        if (StringUtils.isBlank(name)
                || StringUtils.isBlank(configFilePath)) {
            throw new NullPointerException();
        }
        this.name = name;
        this.configFilePath = configFilePath;
        load();
    }

    public FileConfig(String name, InputStream is) {
        this.name = name;
    }

    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    public String getProperty(String key) {
        return properties != null ? properties.getProperty(key) : null;
    }

    /**
     * 从配置文件中读取配置
     */
    public void load() {
        if (StringUtils.isBlank(configFilePath)) {
            return;
        }
        properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(configFilePath));
            properties.load(fis);
            logger.info("成功加载配置文件[" + name + "][" + configFilePath + "].");
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    public void save() {
        if (properties == null || properties.isEmpty()) {
            return;
        }
        FileOutputStream fos = null;
        try {
            FileUtils.createFileWithPath(configFilePath);
            fos = new FileOutputStream(new File(configFilePath));
            properties.store(fos, name);
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    public String getName() {
        return name;
    }

}
