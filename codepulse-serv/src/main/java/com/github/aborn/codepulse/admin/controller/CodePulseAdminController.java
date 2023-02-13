package com.github.aborn.codepulse.admin.controller;

import com.github.aborn.codepulse.admin.datatypes.MonthActionResponse;
import com.github.aborn.codepulse.admin.datatypes.UserActionResponse;
import com.github.aborn.codepulse.api.service.DayBitSetsDataManager;
import com.github.aborn.codepulse.common.datatypes.BaseResponse;
import com.github.aborn.codepulse.common.datatypes.DayBitSet;
import com.github.aborn.codepulse.common.utils.CodePulseDateUtils;
import com.github.aborn.codepulse.common.utils.UserManagerUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 管理后台的接口API
 *
 * @author aborn (jiangguobao)
 * @date 2023/02/10 10:12
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/codepulse/admin")
@AllArgsConstructor
public class CodePulseAdminController {
    private final DayBitSetsDataManager dayBitSetsDataManager;

    // http://127.0.0.1:8080/webx/getUserAction?token=8ba394513f8420e
    @RequestMapping(value = "getUserAction")
    @ResponseBody
    public BaseResponse<UserActionResponse> getUserAction(@NonNull String token, String day) {
        if (!UserManagerUtils.isLegal(token)) {
            return BaseResponse.fail("请求失败!", 501);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dayDate;
        try {
            dayDate = simpleDateFormat.parse(day);
        } catch (ParseException e) {
            return BaseResponse.fail("请求失败!", 500);
        }

        // 如果day信息为空，初始化为今天
        if (StringUtils.isBlank(day)) {
            day = CodePulseDateUtils.getTodayDayInfo();
        }

        DayBitSet result = dayBitSetsDataManager.getBitSetData(token, day);
        if (result == null) {
            result = new DayBitSet(dayDate);
        }
        // 201 无数据；  204 token非法；  205 token已过期；
        return BaseResponse.success(new UserActionResponse(result));
    }

    /**
     * http://127.0.0.1:8080/webx/getMonthActionStatus?token=8ba394513f8420e&month=2021-03
     * 获取每月具体到每天的统计数据
     *
     * @param token
     * @param month 月，格式 2020-02
     * @return
     */
    @RequestMapping(value = "getMonthActionStatus")
    @ResponseBody
    public BaseResponse<MonthActionResponse> getMonthActionStatus(@NonNull String token, @NonNull String month) {
        if (!UserManagerUtils.isLegal(token) || StringUtils.isBlank(month)) {
            return BaseResponse.fail("请求失败!", 401);
        }

        String todayInfo = CodePulseDateUtils.getTodayDayInfo();
        DayBitSet todayData = null; // todo dayBitSetsDataManager.getBitSetData(token, todayInfo);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // calendar.get()  TODO

        MonthActionResponse result = new MonthActionResponse();
        String[] mons = month.split("-");
        if (mons.length < 2) {
            return BaseResponse.fail("请求失败!", 402);
        }
        result.setMonth(month);
        return BaseResponse.success(result);
    }

}
