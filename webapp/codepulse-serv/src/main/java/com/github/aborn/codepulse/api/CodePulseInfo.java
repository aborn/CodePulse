package com.github.aborn.codepulse.api;

import com.github.aborn.codepulse.common.datatypes.BaseDTO;
import com.github.aborn.codepulse.common.datatypes.DayBitSet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author aborn (jiangguobao)
 * @date 2023/02/10 13:18
 */
@Data
@NoArgsConstructor
public class CodePulseInfo extends BaseDTO implements Serializable {
    private long id;
    private String token;
    private String day;
    private String codeInfo;

    private int codeTime = 0;

    public CodePulseInfo(DayBitSet dayBitSet) {
        this.codeInfo = dayBitSet.getCodeInfo();
        this.token = dayBitSet.getToken();
        this.day = dayBitSet.getDay();
        this.createTime = new Date();
        this.updateTime = new Date();
        this.codeTime = dayBitSet.codingTimeSeconds();
    }
}
