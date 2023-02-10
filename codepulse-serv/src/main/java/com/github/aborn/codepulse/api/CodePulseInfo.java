package com.github.aborn.codepulse.api;

import com.github.aborn.codepulse.common.datatypes.DayBitSet;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author aborn (jiangguobao)
 * @date 2023/02/10 13:18
 */
@Data
public class CodePulseInfo implements Serializable {
    private long id;
    private String token;
    private String day;
    private String codeInfo;
    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;

    public CodePulseInfo(DayBitSet dayBitSet) {
        this.codeInfo = dayBitSet.getCodeInfo();
        this.token = dayBitSet.getToken();
        this.day = dayBitSet.getDay();
        this.createTime = new Date();
        this.updateTime = new Date();
    }
}
