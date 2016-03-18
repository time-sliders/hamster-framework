package com.noob.storage.utils;

import java.io.File;
import java.text.SimpleDateFormat;

public class Constant {

    /**
     * 精确到秒的时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static final SimpleDateFormat FMT_SEC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 精确到毫秒的时间格式 yyyy-MM-dd HH:mm:ss:SSS
     */
    public static final SimpleDateFormat FMT_MILSEC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    /**
     * 精确到毫秒的时间格式 中间没有符号 yyyy_MM_dd_HH_mm_ss_SSS
     */
    public static final SimpleDateFormat FMT_MILSEC_NO_BLANK = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");

    /**
     * 精确到天的十位数日期格式 yyyy-MM-dd
     */
    public static final SimpleDateFormat FMT_DAY_10_1 = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 精确到天的十位数日期格式 yyyy/MM/dd
     */
    public static final SimpleDateFormat FMT_DAY_10_2 = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * 精确到天的8位数日期格式 yyyyMMdd
     */
    public static final SimpleDateFormat FMT_DAY_8 = new SimpleDateFormat("yyyyMMdd");

    /**
     * 系统WebRoot目录绝对路径
     */
    public static final String SYSTEM_WEBROOT_PATH = System.getProperty("user.dir")
            + File.separatorChar + "WebRoot" + File.separatorChar;

    /**
     * 系统classes目录绝对路径
     */
    public static final String SYSTEM_CLASSES_PATH = Constant.class.getResource("/").getPath();

    /**
     * extjs Grid默认的起始行数
     */
    public static final int DEFAULT_GRID_START = 0;
    /**
     * extjs Grid默认的每页数量
     */
    public static final int DEFAULT_GRID_LIMIT = 25;

}
