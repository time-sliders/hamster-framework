package com.noob.storage.common.config;

import com.noob.storage.exception.ProcessException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 一个读取指定目录配置的文件属性加载器<br/>
 * <strong>支持动态从文件中写入/删除属性</strong>
 *
 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
 */
public class FileConfig {

    private static Logger logger = LoggerFactory.getLogger(FileConfig.class);

    /**
     * 配置文件存放的路径
     */
    private String location = null;

    private String name = null;

    /**
     * 读取到的配置
     */
    private Properties properties;

    public FileConfig(String name, String location) {

        if (StringUtils.isBlank(name)
                || StringUtils.isBlank(location)) {
            throw new NullPointerException();
        }

        this.name = name;
        this.location = location;

        load();
    }

    public void add(String key, String value) {

        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return;
        }

        if (!value.equals(properties.put(key, value))) {
            save();
            logger.info("向配置文件[{}]:[{}]中添加属性key:{},value:{}",
                    new Object[]{name, location, key, value});
        }
    }

    public void remove(String key) {
        if (StringUtils.isNotBlank(key) && properties.remove(key) != null) {
            save();
            logger.info("从配置文件[{}]:[{}]中删除属性key:{}",
                    new Object[]{name, location, key});
        }
    }

    public String getProperty(String key) {

        if (StringUtils.isBlank(key)) {
            return null;
        }

        return properties.getProperty(key);
    }

    /**
     * 从配置文件中读取配置
     */
    public void load() {
        if (StringUtils.isBlank(location)) {
            return;
        }
        properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(location));
            properties.load(fis);
            logger.info("成功加载配置文件[{}][{}].", new Object[]{name, location});
        } catch (Exception e) {
            throw new ProcessException(e);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    private void save() {
        if (properties == null || properties.isEmpty()) {
            return;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(location));
            properties.store(fos, name);
        } catch (Exception e) {
            throw new ProcessException(e);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    public String getName() {
        return name;
    }

}
