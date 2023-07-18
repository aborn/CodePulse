package com.github.aborn.codepulse.common.datatypes;

import lombok.Data;

import java.util.Date;

/**
 * @author aborn (jiangguobao)
 * @date 2023/07/18 12:04
 */
@Data
public class BaseDTO {
    protected Date createTime;
    protected Date updateTime;
    protected String createBy = "auto";
    protected String updateBy = "auto";
}
