package com.github.aborn.codepulse.admin.datatypes;

import lombok.Data;

/**
 * @author aborn (jiangguobao)
 * @date 2023/02/10 10:12
 */
@Data
public class MonthActionResponse {

    String month;                    // 所有在月份，以 2021-02 这样的格式
    TipItem[] dayStatic = new TipItem[31];   // 每个月的天计,第一天是0

    public MonthActionResponse() {
        for (int i = 0; i < dayStatic.length; i++) {
            dayStatic[i] = new TipItem();
        }
    }

    public int getDay(int day) {
        return dayStatic[day].getDot();
    }

    public void setDay(int day, int value) {
        if (day >= 31 || day < 0) {
            return;
        }
        dayStatic[day].setDot(value);
    }

    public void setType(int day, int type) {
        if (day >= 31 || day < 0) {
            return;
        }
        dayStatic[day].setType(type);
    }
}
