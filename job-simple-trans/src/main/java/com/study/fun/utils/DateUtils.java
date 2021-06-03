package com.study.fun.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 日期处理 工具类
 * </p>
 *
 */
public class DateUtils {
    public static final String DEFAULT_TIME_PATERN = "yyyy-MM-dd HH:mm:ss";

    public static String getDateTime() {
        return getDateTime(DEFAULT_TIME_PATERN);
    }

    public static String getDateTime(String pattern) {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static Date getTodayEndTime() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date d = calendar.getTime();
        return d;
    }

}
