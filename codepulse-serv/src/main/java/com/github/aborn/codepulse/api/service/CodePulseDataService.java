package com.github.aborn.codepulse.api.service;

import com.github.aborn.codepulse.api.CodePulseInfo;
import com.github.aborn.codepulse.api.CodePulseMapper;
import com.github.aborn.codepulse.common.DataService;
import com.github.aborn.codepulse.common.datatypes.DayBitSet;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author aborn (jiangguobao)
 * @date 2023/02/10 10:30
 */
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull DayBitSet dayBitSet) {
        String token = dayBitSet.getToken();
        String day = dayBitSet.getToken();

        CodePulseInfo codePulseInfo = codePulseMapper.findByTokenAndDay(token, day);
        if (codePulseInfo == null) {
            codePulseInfo = new CodePulseInfo(dayBitSet);
            codePulseMapper.insert(codePulseInfo);
        } else {
            // 说明数据存在，做下or操作，防止从不同的渠道过来打点不一致导致当前天历史数据丢失
            DayBitSet dayBitSetDB = new DayBitSet(codePulseInfo);
            dayBitSet.or(dayBitSetDB);
            codePulseInfo.setCodeInfo(dayBitSet.getCodeInfo());
            codePulseMapper.update(codePulseInfo);
        }
    }

    @Override
    public DayBitSet get(String token, String day) {
        return null;
    }
}
