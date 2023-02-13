package com.github.aborn.codepulse.admin.datatypes;

import com.github.aborn.codepulse.common.datatypes.DayBitSet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aborn (jiangguobao)
 * @date 2023/02/13 14:50
 */
@Data
@NoArgsConstructor
public class WeekDayItem {
    public static final Map<Integer, String> WEEK_DAY = new HashMap<>();
    static {
        // dayOfWeek 是从周日开始算起的，周日是1
        WEEK_DAY.put(2, "一");
        WEEK_DAY.put(3, "二");
        WEEK_DAY.put(4, "三");
        WEEK_DAY.put(5, "四");
        WEEK_DAY.put(6, "五");
        WEEK_DAY.put(7, "六");
        WEEK_DAY.put(1, "日");
    }
    private String day;

    private int dayOfWeek;

    private String title;
    private UserActionResponse action;

    public WeekDayItem(String day, int dayOfWeek) {
        this.day = day;
        this.dayOfWeek = dayOfWeek;
        this.title = day + "（周" + WEEK_DAY.get(dayOfWeek) + "）";
    }

    public void setDefaultDayBitSet(String token) {
        this.action =  new UserActionResponse(new DayBitSet(this.day, token));
    }

    public void setAction(DayBitSet data) {
        this.action = new UserActionResponse(data);
    }
}
