package com.github.aborn.codepulse.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author aborn
 * @date 2023/02/10 10:03
 */
public class CodePulseDateUtils {
    public static final long TIMEOUT = 60*1000;

    /**
     * 当date为null时默认为今天
     * @param date
     * @return
     */
    public static String getDayInfo(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return date == null ? simpleDateFormat.format(new Date()) : simpleDateFormat.format(date);
    }

    public static String getTodayDayInfo() {
        return getDayInfo(new Date());
    }

    public static boolean isToday(String day) {
        return getTodayDayInfo().equals(day);
    }

    public static boolean isPersistTimeout(Date cachePersistDate) {
        long currentTime = System.currentTimeMillis();
        long cacheTime = cachePersistDate.getTime();
        // 超时时间设置为1分钟
        return currentTime - cacheTime > TIMEOUT;
    }
}
