package com.github.aborn.codepulse.api.service;

import com.github.aborn.codepulse.common.datatypes.BaseResponse;
import com.github.aborn.codepulse.common.datatypes.DayBitSet;
import com.github.aborn.codepulse.common.utils.CodePulseDateUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author aborn
 * @date 2023/02/10 10:25
 */
@Slf4j
@Service
@AllArgsConstructor
public class DayBitSetsDataManager {
    private static final int MAX_USER_CACHE_SIZE = 2000;

    private final CodePulseDataService dataService;

    /**
     * 只存储当前的数据, key为用户token
     */
    private static final Cache<String, CacheData<DayBitSet>> cache = Caffeine.newBuilder()
            // 初始数量
            .initialCapacity(10)
            // 最大条数
            .maximumSize(MAX_USER_CACHE_SIZE)
            // 最后一次读或写操作后经过指定时间过期
            .expireAfterAccess(24, TimeUnit.HOURS)
            //监听缓存被移除
            .removalListener((key, val, removalCause) -> {
            })
            //记录命中
            .recordStats()
            .build();

    /**
     * 用户上报数据
     *
     * @param dayBitSet
     * @return
     */
    public BaseResponse<String> postBitSetData(@NonNull DayBitSet dayBitSet) {
        // TODO 只能上报今天的，这里有一个本地时间时区问题
        if (!dayBitSet.isToday()) {
            return BaseResponse.fail("Post failed: time error", 501);
        }

        String token = dayBitSet.getToken();
        CacheData<DayBitSet> cacheData = cache.getIfPresent(token);
        if (cacheData != null) {
            // 内存缓存 存在时
            DayBitSet dayBitSetCached = cacheData.getData();
            if (dayBitSetCached.isToday()) {
                dayBitSetCached.or(dayBitSet);
                cacheData.setData(dayBitSetCached);
                cache.put(token, cacheData);
                if (cacheData.getPersistTime() == null || CodePulseDateUtils.isPersistTimeout(cacheData.getPersistTime())) {
                    log.info("{}: 内存缓存超时，持久化到DB", token);
                    DayBitSet dayBitSetSaved = dataService.save(dayBitSet);
                    cacheData.setData(dayBitSetSaved);
                    cacheData.updatePersistTime();
                    cache.put(token, cacheData);
                }
            } else {
                // 缓存里的数据已经不是今天的数据，持久化到数据库
                log.info("{}: 内存数据非今天数据，持久化到DB", token);
                DayBitSet dayBitSetSaved = dataService.save(dayBitSet);
                cacheData.setData(dayBitSetSaved);
                cacheData.updatePersistTime();
                cache.put(token, cacheData);
            }
        } else {
            // 内存缓存数据不存在
            log.info("{}: 内存不存在，从DB里获取", token);
            DayBitSet dayBitSetDb = dataService.get(dayBitSet.getToken(), dayBitSet.getDay());
            if (dayBitSetDb == null) {
                dayBitSetDb = new DayBitSet(dayBitSet);
            }
            dayBitSetDb.or(dayBitSet);
            cacheData = new CacheData<>();
            cacheData.setData(dayBitSetDb);
            // 更新数据库
            dataService.save(dayBitSetDb);
            cacheData.updatePersistTime();
            // 更新本地缓存
            cache.put(token, cacheData);
        }
        return BaseResponse.success("Post Success");
    }

    public DayBitSet getBitSetData(@NonNull String token, @NonNull String day) {
        if (!CodePulseDateUtils.isToday(day)) {
            // 说明获取的历史天的数据，直接从数据库里获取
            return dataService.get(token, day);
        }

        CacheData<DayBitSet> cacheData = cache.getIfPresent(token);
        if (cacheData != null) {
            DayBitSet dayBitSet = cacheData.getData();
            if (dayBitSet.isToday()) {
                return dayBitSet;
            } else {
                // 数据不是今天，存储起来，因为可能隔天了
                dataService.save(dayBitSet);
                return dataService.get(token, day);
            }
        } else {
            // 直接从数据库里获取，获取后保存到本地缓存里
            DayBitSet dayBitSet = dataService.get(token, day);
            if (dayBitSet != null) {
                cacheData = new CacheData<>();
                cacheData.setData(dayBitSet);
                cache.put(token, cacheData);
            }
            return dayBitSet;
        }
    }

    public List<String> cachedTokens() {
        List<String> result = new ArrayList<>();
        return result;
    }

    @PostConstruct
    public void init() {

    }

    @PreDestroy
    public void saveStaticsBeforeShutDown() {

    }
}
