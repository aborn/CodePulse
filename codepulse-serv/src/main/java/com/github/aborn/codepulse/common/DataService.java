package com.github.aborn.codepulse.common;

import com.github.aborn.codepulse.common.datatypes.DayBitSet;

import java.util.List;

/**
 * 处理数据的接口
 *
 * @author aborn (jiangguobao)
 * @date 2023/02/10 10:29
 */
public interface DataService {

    DayBitSet save(DayBitSet dayBitSet);

    DayBitSet get(String token, String day);

    List<DayBitSet> queryList(String token, List<String> days);
}
