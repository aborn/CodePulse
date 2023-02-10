package com.github.aborn.codepulse.api.controller;

import com.alibaba.fastjson2.JSONObject;
import com.github.aborn.codepulse.api.CodePulseInfo;
import com.github.aborn.codepulse.api.UserActionRequest;
import com.github.aborn.codepulse.api.service.CodePulseDataService;
import com.github.aborn.codepulse.api.service.DayBitSetsDataManager;
import com.github.aborn.codepulse.common.datatypes.BaseResponse;
import com.github.aborn.codepulse.common.datatypes.DayBitSet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

/**
 * 作为各个插件上报打点数据的API
 *
 * @author aborn (jiangguobao)
 * @date 2023/02/10 09:48
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/codepulse/v1/")
@AllArgsConstructor
public class CodePulseApiController {
    private static Date bootTime = null;

    private final DayBitSetsDataManager dayBitSetsDataManager;

    private final CodePulseDataService codePulseDataService;

    @PostMapping(value = "userAction")
    @ResponseBody
    public BaseResponse<String> postUserAction(@RequestBody UserActionRequest request) {
        // TODO token 校验，校验不通过直接返回
        log.info("Request, content{}", JSONObject.toJSONString(request));
        DayBitSet dayBitSet = new DayBitSet(request.getDay(), request.getDayBitSetArray(), request.getToken());
        if (dayBitSet.isEmptySlot()) {
            return BaseResponse.fail("编程数据为空slotCount=0", 402);
        }

        // 用户上报数据存储
        return dayBitSetsDataManager.postBitSetData(dayBitSet);
    }

    // http://127.0.0.1:8000/api/codepulse/v1/status
    @RequestMapping(value = "status")
    @ResponseBody
    public String status() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map = new HashMap<>();
        map.put("status", "success");
        map.put("app", "codepulse");
        map.put("timestamp", simpleDateFormat.format(new Date()));
        map.put("boot_time", bootTime == null ? "null" : simpleDateFormat.format(bootTime));
        return JSONObject.toJSONString(map);
    }

    // http://127.0.0.1:8080/webx/test
    @RequestMapping(value = "test")
    @ResponseBody
    public String test(Integer slot) {
        DayBitSet currentDayBitSet = new DayBitSet();
        if (currentDayBitSet == null) {
            return "current is null";
        }

        if (slot != null) {
            currentDayBitSet.set(slot);
        } else {
            currentDayBitSet.setSlotByCurrentTime();
        }
        return currentDayBitSet.toString();
    }

    @PostConstruct
    public void init() {
        bootTime = new Date();
    }

}
