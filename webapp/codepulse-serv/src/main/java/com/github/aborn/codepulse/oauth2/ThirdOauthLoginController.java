package com.github.aborn.codepulse.oauth2;

import com.alibaba.fastjson2.JSONObject;
import com.github.aborn.codepulse.admin.datatypes.UserActionResponse;
import com.github.aborn.codepulse.common.datatypes.BaseResponse;
import com.github.aborn.codepulse.common.utils.FileConfigUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aborn (jiangguobao)
 * @date 2023/07/13 20:17
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/codepulse/oauth2")
@AllArgsConstructor
public class ThirdOauthLoginController {

    private static final String LOGIN_URL = "https://github.com/login/oauth/access_token";

    /**
     * 调用第三方登录
     * code=063c73729e3f07ef5fe8  用于获取accessToken
     * state=88bb66aa  用于校验
     *
     * @param code
     * @param state
     * @return
     */
    @RequestMapping(value = "redirect")
    @ResponseBody
    public BaseResponse<Object> getUserAction(@NonNull String code, String state) {
        /**
         * client_id: string
         *     client_secret: string
         *     code: string
         *     redirect_uri: string
         */

        Map<String, Object> data = new HashMap<>();
        data.put("client_id", "2645bbcd62a78528da2a");
        data.put("client_secret", FileConfigUtils.getClientSecrets());
        data.put("code", code);
        // data.put("redirect_uri", )

        try {
            String accessToken = getAccessToken(data);
        } catch (Exception e) {
            log.error("Get access token failed.");
        }

        return BaseResponse.success("good");
    }

    /**
     * @param data
     * @throws Exception
     */
    // https://openjdk.org/groups/net/httpclient/recipes.html#post
    private String getAccessToken(Map<String, Object> data) throws Exception {
        String api = "https://github.com/login/oauth/access_token";
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api))
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofForm(data))
                .build();

        HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.discarding());

        System.out.println(response);
        return "accessToken";
    }

    public static HttpRequest.BodyPublisher ofForm(Map<String, Object> data) {
        StringBuilder body = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (body.length() > 0) {
                body.append("&");
            }
            body.append(encode(entry.getKey())).append("=").append(encode(entry.getValue()));
        }
        return HttpRequest.BodyPublishers.ofString(body.toString());
    }

    private static String encode(Object obj) {
        return URLEncoder.encode(obj.toString(), StandardCharsets.UTF_8);
    }
}
