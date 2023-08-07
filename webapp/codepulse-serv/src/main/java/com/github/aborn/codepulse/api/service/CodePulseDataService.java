package com.github.aborn.codepulse.api.service;

import com.github.aborn.codepulse.api.CodePulseInfo;
import com.github.aborn.codepulse.api.CodePulseMapper;
import com.github.aborn.codepulse.common.DataService;
import com.github.aborn.codepulse.common.datatypes.DayBitSet;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author aborn (jiangguobao)
 * @date 2023/02/10 10:30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CodePulseDataService implements DataService {
    private final CodePulseMapper codePulseMapper;

    public CodePulseInfo findById(long id) {
        CodePulseInfo codePulseInfo = codePulseMapper.findById(id);
        if (codePulseInfo == null) {
            throw new ResourceNotFoundException("id", id);
        }
        return codePulseInfo;
    }

    /**
     * 持久化数据到数据库，返回最新数据，不修改原始传入数据
     *
     * @param dayBitSet
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DayBitSet save(@NonNull DayBitSet dayBitSet) {
        DayBitSet result;
        String token = dayBitSet.getToken();
        String day = dayBitSet.getDay();

        CodePulseInfo codePulseInfo = codePulseMapper.findByTokenAndDay(token, day);
        if (codePulseInfo == null) {
            codePulseInfo = new CodePulseInfo(dayBitSet);
            codePulseMapper.insert(codePulseInfo);
            result = new DayBitSet(dayBitSet);
        } else {
            // 说明数据存在，做下or操作，防止从不同的渠道过来打点不一致导致当前天历史数据丢失
            result = new DayBitSet(codePulseInfo);
            result.or(dayBitSet);
            // codePulseInfo.setCodeInfo(result.getCodeInfo());
            codePulseInfo.setCodeInfo(result.getCodeInfoByLong());
            codePulseInfo.setUpdateTime(new Date());
            codePulseInfo.setCodeTime(result.codingTimeSeconds());
            codePulseMapper.update(codePulseInfo);
        }
        return result;
    }

    @Override
    public DayBitSet get(String token, String day) {
        CodePulseInfo codePulseInfo = codePulseMapper.findByTokenAndDay(token, day);
        return codePulseInfo == null ? null : new DayBitSet(codePulseInfo);
    }

    @Override
    public List<DayBitSet> queryList(String token, List<String> days) {
        List<CodePulseInfo> infoList = codePulseMapper.queryList(token, days);
        List<DayBitSet> result = new ArrayList<>();
        infoList.forEach(item -> result.add(new DayBitSet(item)));
        return result;
    }

    @Override
    public List<CodePulseInfo> queryDailyTrending(String day) {
        return codePulseMapper.queryDailyTrending(day);
    }
}
