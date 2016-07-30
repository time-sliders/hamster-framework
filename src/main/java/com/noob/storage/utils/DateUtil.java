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

    public static final String hhMMss = "hhMMss";

    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;

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
        return addSeconds(date1, days * ONE_DAY);
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

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat(DEFAULT_FORMAT);
        Date date1 = fmt.parse("2015-01-01 10:20:30");
        Date date2 = fmt.parse("2016-02-01 10:20:30");
        Date date3 = fmt.parse("2016-06-10 10:20:30");
        Date date4 = fmt.parse("2015-06-16 10:20:30");
        Date date5 = fmt.parse("2015-06-16 17:20:30");
        System.out.println(fromToday(date1));
        System.out.println(fromToday(date2));
        System.out.println(fromToday(date3));
        System.out.println(fromToday(date4));
        System.out.println(fromToday(date5));
    }

    /**
     * 距离今天多久
     */
    public static String fromToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        long time = date.getTime() / 1000;
        long now = new Date().getTime() / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟前";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE)
                    + "分钟前";
        else if (ago <= ONE_DAY * 2)
            return "昨天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
                    + calendar.get(Calendar.MINUTE) + "分";
        else if (ago <= ONE_DAY * 3)
            return "前天" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
                    + calendar.get(Calendar.MINUTE) + "分";
        else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            return day + "天前" + calendar.get(Calendar.HOUR_OF_DAY) + "点"
                    + calendar.get(Calendar.MINUTE) + "分";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            return month + "个月" + day + "天前"
                    + calendar.get(Calendar.HOUR_OF_DAY) + "点"
                    + calendar.get(Calendar.MINUTE) + "分";
        } else {
            long year = ago / ONE_YEAR;
            int month = calendar.get(Calendar.MONTH) + 1;
            return year + "年前" + month + "月" + calendar.get(Calendar.DATE)
                    + "日";
        }

    }

    /**
     * 距离截止日期还有多长时间
     */
    public static String fromDeadline(Date date) {
        long deadline = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long remain = deadline - now;
        if (remain <= ONE_HOUR)
            return "只剩下" + remain / ONE_MINUTE + "分钟";
        else if (remain <= ONE_DAY)
            return "只剩下" + remain / ONE_HOUR + "小时"
                    + (remain % ONE_HOUR / ONE_MINUTE) + "分钟";
        else {
            long day = remain / ONE_DAY;
            long hour = remain % ONE_DAY / ONE_HOUR;
            long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return "只剩下" + day + "天" + hour + "小时" + minute + "分钟";
        }
    }

    public static long getDiffDays(Date before, Date after) {

        Calendar c1 = Calendar.getInstance();
        c1.setTime(before);
        ignoreTime(c1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(after);
        ignoreTime(c2);

        return (c2.getTimeInMillis() - c1.getTimeInMillis())
                / (24 * 60 * 60 * 1000);
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

}
