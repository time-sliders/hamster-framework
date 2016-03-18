package com.noob.storage.utils;

import com.noob.storage.exception.ProcessException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils {

    private static Logger logger = Logger.getLogger(DateUtils.class);

    /**
     * 注意 SimpleDateFormat类是线程不安全的，所以这个方法添加了同步锁
     * 如果在多线程开发中想提升效率，需要在每个线程中实例化一个SimpleDateFormat实例
     */
    public synchronized static Date formatStr2Date(String dateStr) {
        Date date = null;
        try {
            if (StringUtils.isNotBlank(dateStr)) {
                if (dateStr.length() == 10) {
                    if (dateStr.contains("-")) {//yyyy-MM-dd
                        date = Constant.FMT_DAY_10_1.parse(dateStr);
                    } else if (dateStr.contains("/")) {//[yyyy/MM/dd]
                        date = Constant.FMT_DAY_10_2.parse(dateStr);
                    }
                } else if (dateStr.length() == 8) {//yyyyMMdd
                    date = Constant.FMT_DAY_8.parse(dateStr);
                } else if (dateStr.length() == 19) {//yyyy-MM-dd HH:mm:ss
                    if (dateStr.contains("T")) {
                        dateStr = dateStr.replaceAll("T", " ");
                    }
                    date = Constant.FMT_SEC.parse(dateStr);
                } else if (dateStr.length() == 23) {//yyyy-MM-dd HH:mm:ss:SSS
                    date = Constant.FMT_MILSEC.parse(dateStr);
                } else {
                    logger.warn("没有符合条件的日期格式与传入日期字符串相匹配![" + dateStr + "]");
                }
            }
        } catch (Exception e) {
            logger.error("程序在将日期字符串转换为日期对象时出现异常!", e);
            throw new ProcessException("程序在将日期字符串转换为日期对象时出现异常!");
        }
        return date;
    }

}
