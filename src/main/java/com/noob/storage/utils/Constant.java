package com.noob.storage.utils;

import java.io.File;
import java.text.SimpleDateFormat;

public class Constant {

    /**
     * 系统WebRoot目录绝对路径
     */
    public static final String SYSTEM_WEBROOT_PATH = System.getProperty("user.dir")
            + File.separatorChar + "WebRoot" + File.separatorChar;

    /**
     * 系统classes目录绝对路径
     */
    public static final String SYSTEM_CLASSES_PATH = Constant.class.getResource("/").getPath();

}
