package com.github.aborn.codepulse.admin.controller;

import com.github.aborn.codepulse.admin.datatypes.MonthActionResponse;
import com.github.aborn.codepulse.admin.datatypes.TrendingResponse;
import com.github.aborn.codepulse.admin.datatypes.UserActionResponse;
import com.github.aborn.codepulse.admin.datatypes.WeekDayItem;
import com.github.aborn.codepulse.api.CodePulseInfo;
import com.github.aborn.codepulse.api.service.CodePulseDataService;
import com.github.aborn.codepulse.api.service.DayBitSetsDataManager;
import com.github.aborn.codepulse.common.datatypes.BaseResponse;
import com.github.aborn.codepulse.common.datatypes.DayBitSet;
import com.github.aborn.codepulse.common.utils.CodePulseDateUtils;
import com.github.aborn.codepulse.common.utils.UserManagerUtils;
import com.github.aborn.codepulse.oauth2.datatypes.UserInfo;
import com.github.aborn.codepulse.oauth2.service.UserInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private final CodePulseDataService dataService;

    private final UserInfoService userInfoService;

    /**
     * 每天的统计数据
     * http://127.0.0.1:8081/api/v1/codepulse/admin/getUserAction?token=8ba394513f8420e&day=2023-02-10
     *
     * @param token
     * @param day
     * @return
     */
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

    @RequestMapping(value = "getUserTrending")
    @ResponseBody
    public BaseResponse<TrendingResponse> getUserTrending(String token, String day) {
        // 当token存在时返回公司的？？
        // if (StringUtils.isNotBlank(token)) {
        //    return BaseResponse.fail("请求失败!", 501);
        //}
        TrendingResponse response = TrendingResponse.builder().build();
        List<CodePulseInfo> data = dataService.queryDailyTrending(day);

        if (!CollectionUtils.isEmpty(data)) {
            for (CodePulseInfo item : data) {
                UserInfo userInfo = userInfoService.queryUserInfo(item.getToken());
                response.add(new DayBitSet(item).codingTimeMinutes(), userInfo.getName(), userInfo.getAvatar());
            }
        }
        return BaseResponse.success(response);
    }


    /**
     * 每周的统计数据
     * http://127.0.0.1:8001/api/v1/codepulse/admin/getWeekUserAction?token=0x4af97338
     *
     * @param token
     * @param day   以客户端为准的今日信息 （因为服务端有可能与客户端所处的时区不一样）， 以这个信息往前一周
     * @return
     */
    @RequestMapping(value = "getWeekUserAction")
    @ResponseBody
    public BaseResponse<List<WeekDayItem>> getWeekUserAction(@NonNull String token, String day) throws ParseException {
        if (!UserManagerUtils.isLegal(token)) {
            return BaseResponse.fail("请求失败!", 501);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dayInfo = StringUtils.isBlank(day) ? sdf.format(new Date()) : day;
        DayBitSet todayData = dayBitSetsDataManager.getBitSetData(token, dayInfo);
        if (todayData == null) {
            todayData = new DayBitSet();
        }

        // 补充过去6天的数据
        Calendar calendar = Calendar.getInstance();
        Date dayDate = sdf.parse(dayInfo);
        calendar.setTime(dayDate);
        List<WeekDayItem> result = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            calendar.add(Calendar.DATE, -i);
            String dayStr = sdf.format(calendar.getTime());
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            WeekDayItem weekDayItem = new WeekDayItem(dayStr, dayOfWeek);
            result.add(weekDayItem);
            calendar.setTime(dayDate);
            if (i == 0) {
                // 处理当天数据
                weekDayItem.setAction(todayData);
            }
        }

        List<DayBitSet> dayBitSetList = dataService.queryList(token, result.stream().map(WeekDayItem::getDay).collect(Collectors.toList()));
        Map<String, DayBitSet> dayBitSetMap = dayBitSetList.stream().collect(Collectors.toMap(DayBitSet::getDay, Function.identity()));
        result.forEach(item ->
                item.setAction(dayBitSetMap.containsKey(item.getDay()) ?
                        dayBitSetMap.get(item.getDay())
                        : new DayBitSet(item.getDay(), token)));

        return BaseResponse.success(result);
    }

    /**
     * http://127.0.0.1:8001/api/v1/codepulse/admin/getMonthActionStatus?token=8ba394513f8420e&month=2021-03
     * 获取每月具体到每天的统计数据，提供接口给小程序或者后台使用
     *
     * @param token
     * @param month 月，格式 2020-02
     * @return
     */
    @RequestMapping(value = "getMonthActionStatus")
    @ResponseBody
    public BaseResponse<MonthActionResponse> getMonthActionStatus(@NonNull String token, String month) {
        if (!UserManagerUtils.isLegal(token)) {
            return BaseResponse.fail("请求失败!", 401);
        }

        if (StringUtils.isBlank(month)) {
            // 默认为当月
            month = CodePulseDateUtils.getMonthInfo(new Date());
        }

        String todayInfo = CodePulseDateUtils.getTodayDayInfo();
        DayBitSet todayData = dayBitSetsDataManager.getBitSetData(token, todayInfo);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // calendar.get()  TODO

        MonthActionResponse result = new MonthActionResponse();
        String[] mons = month.split("-");
        if (mons.length < 2) {
            return BaseResponse.fail("请求失败!", 402);
        }
        result.setMonth(month);

        for (int day = 0; day < result.getDayStatic().length; day++) {
            String dayInfo = day < 9 ? month + "-0" + (day + 1) : month + "-" + (day + 1);
            DayBitSet dayBitSet = dayBitSetsDataManager.getBitSetDataFromDB(token, dayInfo);
            if (dayBitSet != null && dayBitSet.countOfCodingSlot() > 0) {
                result.setDay(day, dayBitSet.codingTimeSeconds());
            }

            // 处理今天
            if (result.getDay(day) == 0 && todayInfo.equals(dayInfo)
                    && todayData != null && !todayData.isEmptySlot()) {
                result.setDay(day, todayData.codingTimeSeconds());
            }
        }
        return BaseResponse.success(result);
    }

}
