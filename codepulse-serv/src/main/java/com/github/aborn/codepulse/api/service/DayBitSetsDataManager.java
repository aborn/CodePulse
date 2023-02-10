package com.github.aborn.codepulse.api.service;

import com.github.aborn.codepulse.common.datatypes.BaseResponse;
import com.github.aborn.codepulse.common.datatypes.DayBitSet;
import com.github.aborn.codepulse.common.utils.CodePulseDateUtils;
import com.github.aborn.codepulse.common.utils.UserManagerUtils;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author aborn
 * @date 2023/02/10 10:25
 */
@Service
@AllArgsConstructor
public class DayBitSetsDataManager {

    private final CodePulseDataService dataService;

    /**
     * 只存储当前的数据, key为用户token（用户id）
     */
    private static final ConcurrentHashMap<String, DayBitSet> DAYBITSETS = new ConcurrentHashMap();
    private static final int MAX_USER_CACHE_SIZE = 1000;

    public DayBitSet getDefaultCachedBitSetData() {
        return DAYBITSETS.get(UserManagerUtils.DEFAULT_TEST_UID_TOKEN);
    }

    public DayBitSet getBitSetData(@NonNull String token, @NonNull String day) {
        DayBitSet result;

        if (CodePulseDateUtils.isToday(day)) {
            DayBitSet dayBitSet = DAYBITSETS.get(token);
            if (dayBitSet == null || !dayBitSet.isToday()) {
                // 数据不是今天，存储起来，因为可能隔天了
                if (dayBitSet != null) {
                    // DataStoreManager.save(dayBitSet);
                }

                // 从文件里读今天的数据
                result = null; // DataStoreManager.read(token, day);
                if (result != null && DAYBITSETS.size() < MAX_USER_CACHE_SIZE) {
                    DAYBITSETS.put(token, result);
                }
            } else {
                result = dayBitSet;
            }
        } else {
            result = null;// DataStoreManager.read(token, day);
        }

        return result;
    }

    /**
     * 用户上报数据
     * @param dayBitSet
     * @return
     */
    public BaseResponse<String> postBitSetData(@NonNull DayBitSet dayBitSet) {
        // TODO 只能上报今天的，这里有一个本地时间时区问题
        if (!dayBitSet.isToday()) {
            return BaseResponse.fail("Post failed: time error", 501);
        }

        String token = dayBitSet.getToken();
        DayBitSet dayBitSetCached = DAYBITSETS.get(token);
        if (dayBitSetCached != null) {
            if (dayBitSetCached.isToday()) {
                dayBitSetCached.or(dayBitSet);
                return BaseResponse.success("Post Success");
            } else {
                // 缓存里的数据已经不是今天的数据，持久化到文件里
                dataService.save(dayBitSet);
                DAYBITSETS.remove(token);
            }
        }

        DayBitSet fileReadCached = null;// ataStoreManager.read(token, dayBitSet.getDay());
        if (fileReadCached != null) {
            fileReadCached.or(dayBitSet);
        } else {
            fileReadCached = dayBitSet;
        }

        if (DAYBITSETS.size() < MAX_USER_CACHE_SIZE) {
            DAYBITSETS.put(token, fileReadCached);
            return BaseResponse.success("Post Success");
        } else {
            System.out.println("max cached. current token=" + token);
            return BaseResponse.fail("Post failed: store error", 502);
        }
    }

    public List<String> cachedTokens() {
        List<String> result = new ArrayList<>();
        DAYBITSETS.forEach((k, v) -> result.add(k));
        return result;
    }

    @PostConstruct
    public void init() {

    }

    @PreDestroy
    public void saveStaticsBeforeShutDown() {

    }
}
