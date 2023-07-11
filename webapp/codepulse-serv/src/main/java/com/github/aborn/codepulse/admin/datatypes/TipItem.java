package com.github.aborn.codepulse.admin.datatypes;

import lombok.Data;

/**
 * @author aborn
 * @date 2023/02/10 10:41
 */
@Data
public class TipItem {
    // >1 表示有编程记录，dot的值为当天的编程时间
    int dot;

    //  1休息，2补班，见DayType
    int type;
}
