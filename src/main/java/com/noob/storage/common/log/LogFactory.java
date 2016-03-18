package com.noob.storage.common.log;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RootLogger;

import java.io.File;

/**
 * log4j
 */
public class LogFactory {

    /**
     * logger仓库
     */
    private static LoggerRepository hierarchy = new Hierarchy(new RootLogger(Level.ALL));

    /**
     * log4j配置文件路径
     */
    private static String path = System.getProperty("user.dir") + File.separatorChar
            + "config" + File.separatorChar + "log4j.properties";

    static {
        /**加载配置文件*/
        new PropertyConfigurator().doConfigure(path, hierarchy);
    }

    public static Logger getLogger(Class<?> clazz) {
        return hierarchy.getLogger(clazz.getName());
    }

}
