package com.github.aborn.codepulse.oauth2;

import com.alibaba.fastjson2.JSONObject;
import com.github.aborn.codepulse.common.datatypes.BaseResponse;
import com.github.aborn.codepulse.common.utils.FileConfigUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    private static final String GITHUB_CLIENT_ID = "2645bbcd62a78528da2a";

    /**
     * 通过code和state 获取用户信息
     * code=063c73729e3f07ef5fe8  用于获取accessToken
     * state=88bb66aa  用于校验
     *
     * @param code
     * @param state
     * @return
     */
    @RequestMapping(value = "getUserInfo")
    @ResponseBody
    public BaseResponse<Object> getUserAction(@NonNull String code, String state) {
        Map<String, Object> data = new HashMap<>();
        data.put("client_id", GITHUB_CLIENT_ID);
        data.put("client_secret", FileConfigUtils.getClientSecrets());
        data.put("code", code);
        // data.put("redirect_uri", )

        try {
            String accessToken = getAccessToken(data);
            if (accessToken != null) {
                String userInfo = getUserInfo(accessToken);
                System.out.println(userInfo);
            }
        } catch (Exception e) {
            log.error("Get access token failed.");
        }

        return BaseResponse.success("good");
    }

    /**
     * 通过accessToken获取用户信息
     *
     * @param accessToken
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private String getUserInfo(String accessToken) throws IOException, InterruptedException {
        String api = "https://api.github.com/user";
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api))
                .header("Accept", "application/json")
                .header("Authorization", "token " + accessToken)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            return jsonObject.toJSONString();
        } else {
            System.out.println("Status code: " + response.statusCode());
            System.out.println("\n Body: " + response.body());
            return null;
        }
    }

    /**
     * 通过code获取accessToken
     *
     * @param data
     * @throws Exception
     */
    // https://openjdk.org/groups/net/httpclient/recipes.html#post
    private String getAccessToken(Map<String, Object> data) throws IOException, InterruptedException {
        String api = "https://github.com/login/oauth/access_token";
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api))
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofForm(data))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            return jsonObject.getString("access_token");
        } else {
            System.out.println("Status code: " + response.statusCode());
            System.out.println("\n Body: " + response.body());
            return null;
        }
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
