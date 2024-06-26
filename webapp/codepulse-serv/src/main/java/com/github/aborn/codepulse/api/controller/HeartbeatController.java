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
@RequestMapping(value = "/api/v1/codepulse/")
public class HeartbeatController {

    // http://localhost:8001/api/codepulse/v1/live
    @RequestMapping(value = "live")
    @ResponseBody
    public String live() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map = new HashMap<>();
        map.put("status", "success");
        map.put("app", "codepulse");
        map.put("version", "1");
        map.put("timestamp", simpleDateFormat.format(new Date()));
        map.put("time", System.currentTimeMillis());
        return JSONObject.toJSONString(map);
    }

}
