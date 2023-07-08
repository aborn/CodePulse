package com.github.aborn.codepulse.api.service;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author aborn (jiangguobao)
 * @date 2023/02/10 16:55
 */
@Data
public class CacheData<T> implements Serializable {
    T data;

    /**
     * 持久化到数据库里的时间
     */
    private Date persistTime;

    /**
     * 缓存最新更新时间
     */
    private Date updateTime;

    /**
     * 缓存创建时间
     */
    private Date createTime;

    public void setData(T data) {
        this.data = data;
        this.updateTime = new Date();
        if (this.createTime == null) {
            this.createTime = this.updateTime;
        }
    }

    public void updatePersistTime() {
        this.persistTime = new Date();
    }
}
