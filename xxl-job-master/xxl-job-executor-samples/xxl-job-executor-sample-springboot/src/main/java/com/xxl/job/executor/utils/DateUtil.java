package com.xxl.job.executor.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {

    public static final String BASE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String ZH_TIME_PATTERN = "yyyy年MM月dd HH时mm分ss秒";

    public static final String SS_TIME_PATTERN = "yyyyMMdd";

    public static final String MM_TIME_PATTERN = "yyyyMM";

    public static final String DEFAULT_TIME_PATERN = "yyyyMMddHHmmss";


    public static String getDateTime() {
        return getDateTime(DEFAULT_TIME_PATERN);
    }

    public static String getDateTime(String pattern) {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }


    public static String getDate() {
        LocalDate localDate = LocalDate.now();
        return localDate.toString();
    }


    public static String getTime() {
        LocalTime localTime = LocalTime.now();
        return localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }


    public static Date getDayAfter() {
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        return c.getTime();
    }


    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    public static Date getDateTime(String str, String pattern) {
        try {
            if (StringUtils.isEmpty(str)) {
                return null;
            }
            return new SimpleDateFormat(pattern).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
