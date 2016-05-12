package com.noob.storage.utils;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String FMT_MILL_SEC = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String NO_SECOND_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_M_d = "yyyy-M-d";
    public static final String yy_MM_dd = "yy-MM-dd";
    public static final String MM_dd = "MM-dd";

    public static final String CHINESE_DATE_FORMAT = "yyyy年MM月dd日";
    public static final String SHORT_CHINESE_DATE_FORMAT = "M月d日";

    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String yyyyMd_point = "yyyy.M.d";
    public static final String yyyyMMdd_slash = "yyyy/MM/dd";
    public static final String yyyyMd_slash = "yyyy/M/d";

    public final static long ONE_DAY_SECONDS = 86400;

    public static String format(Date date, String format) {

        if (date == null || StringUtils.isBlank(format)) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    public static String getDateString(Date date, DateFormat dateFormat) {

        if (date == null || dateFormat == null) {
            return null;
        }

        return dateFormat.format(date);
    }

    public static Date addHours(Date date, long hours) {
        return addMinutes(date, hours * 60);
    }

    public static Date addMinutes(Date date, long minutes) {
        return addSeconds(date, minutes * 60);
    }

    public static Date addDays(Date date1, long days) {
        return addSeconds(date1, days * ONE_DAY_SECONDS);
    }

    public static Date addSeconds(Date date, long secs) {
        return new Date(date.getTime() + (secs * 1000));
    }

    /**
     * 获取一个时间的intValue 如 2016-04-28 －> 20160428
     *
     * @param date 指定的时间
     */
    public Integer getDateIntValue(Date date) {

        if (date == null) {
            return null;
        }

        String dateStr = format(date, yyyyMMdd);

        return Integer.valueOf(dateStr);
    }

    /**
     * 将一个8位日期转换为Date
     */
    public static Date getIntAsDate(Integer date) {

        if (date == null) {
            return null;
        }

        try {
            String dateString = date.toString();
            if (dateString.length() != 8) {
                throw new IllegalArgumentException("date:" + date
                        + " is invalid!");
            }

            return new SimpleDateFormat(yyyyMMdd).parse(date.toString());

        } catch (ParseException ignore) {
            return null;
        }
    }

    /**
     * 将一个日期的时分秒设置为0
     *
     * @param date 指定的时间
     */
    public static void ignoreTime(Date date) {

        if (date == null) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        ignoreTime(calendar);
    }

    /**
     * 将一个日期的时分秒设置为0
     *
     * @param calendar 指定的时间
     */
    public static void ignoreTime(Calendar calendar) {

        if (calendar == null) {
            return;
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static long getDiffDays(Date before, Date after) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(before);
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(after);
        c2.set(Calendar.HOUR_OF_DAY, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);
        return (c2.getTimeInMillis() - c1.getTimeInMillis())
                / (24 * 60 * 60 * 1000);
    }

}
