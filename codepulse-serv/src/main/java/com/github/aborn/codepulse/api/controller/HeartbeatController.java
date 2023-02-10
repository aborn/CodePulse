package com.github.aborn.codepulse.api.controller;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aborn
 * @date 2023/02/10 10:21
 */
@RestController
@RequestMapping(value = "/api/codepulse/v1/")
public class HeartbeatController {

    // http://127.0.0.1:8080/heartbeat/live
    @RequestMapping(value = "live")
    @ResponseBody
    public String live() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map = new HashMap<>();
        map.put("status", "success");
        map.put("app", "buda");
        map.put("timestamp", simpleDateFormat.format(new Date()));
        map.put("time", System.currentTimeMillis());
        return JSONObject.toJSONString(map);
    }

}
