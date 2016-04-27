package com.noob.storage.utils;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

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
