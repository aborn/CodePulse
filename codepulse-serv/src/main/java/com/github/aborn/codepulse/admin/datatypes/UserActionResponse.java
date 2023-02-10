package com.github.aborn.codepulse.admin.datatypes;

import com.github.aborn.codepulse.common.datatypes.DayBitSet;
import lombok.Data;

import java.io.Serializable;

/**
 * @author aborn (jiangguobao)
 * @date 2023/02/10 10:12
 */
@Data
public class UserActionResponse implements Serializable {
    String day;
    String desc;
    String currentHourSlotInfo;
    // 编码时间，单位为S
    int codeTime;
    int[] dayStaticByHour;

    public UserActionResponse(DayBitSet dayBitSet) {
        this.day = dayBitSet.getDay();
        //this.desc = dayBitSet.toString();
        this.codeTime = dayBitSet.codingTimeSeconds();

        double codeMinutes = dayBitSet.codingTimeMinutes();
        int codeHour = (int) codeMinutes / 60;

        if (codeMinutes > 60) {
            this.desc = day + "，编码时间：" + codeHour + "小时" + (codeMinutes - codeHour * 60) + "分钟。";
        } else {
            this.desc = day + "，编码时间：" + codeMinutes + "分钟。";
        }
        this.dayStaticByHour = dayBitSet.getDayStaticByHour();
        this.currentHourSlotInfo = dayBitSet.getCurrentHourSlotInfo();
    }

    public static void main(String[] args) {
        double codeMinutes = 71.5;
        int codeHour = (int) codeMinutes / 60;
        System.out.println(codeHour + "小时" + (codeMinutes - codeHour * 60) + "分钟。");
    }
}
