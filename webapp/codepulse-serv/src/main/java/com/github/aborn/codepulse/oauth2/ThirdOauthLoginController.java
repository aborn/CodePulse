package com.github.aborn.codepulse.oauth2;

import com.alibaba.fastjson2.JSONObject;
import com.github.aborn.codepulse.common.datatypes.BaseResponse;
import com.github.aborn.codepulse.common.utils.FileConfigUtils;
import com.github.aborn.codepulse.oauth2.datatypes.UserInfo;
import com.github.aborn.codepulse.oauth2.service.UserInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    private final UserInfoService userInfoService;

    /**
     * 通过code获取用户信息
     * @param code
     * @return
     */
    @RequestMapping(value = "getUserInfo")
    @ResponseBody
    public BaseResponse<Object> getUserAction(@NonNull String code) {
        Map<String, Object> data = new HashMap<>();
        data.put("client_id", GITHUB_CLIENT_ID);
        data.put("client_secret", FileConfigUtils.getClientSecrets());
        data.put("code", code);
        // data.put("redirect_uri", )

        try {
            String accessToken = getAccessToken(data);
            log.info("Get accessToken github. {}", accessToken);
            if (accessToken != null) {
                JSONObject userInfo = getUserInfo(accessToken);
                log.info("Get User Info from github. {}", userInfo);
                if (userInfo != null) {
                    UserInfo userInfoRes = saveUserInfoToDb(userInfo);
                    return BaseResponse.success(userInfoRes);
                }
                log.info("userInfo = null");
            }
        } catch (Exception e) {
            log.error("Get access token failed. msg={}", e.getMessage());
        }

        return BaseResponse.fail("getUserInfo failed.");
    }

    /**
     * 用户登出接口，待更新
     *
     * @param token
     * @return
     */
    @PostMapping(value = "logout")
    @ResponseBody
    public BaseResponse<Object> logout(@NonNull String token) {
        return BaseResponse.success("good");
    }

    private UserInfo saveUserInfoToDb(@NonNull JSONObject userInfo) {
        UserInfo userInfoDo = new UserInfo(userInfo);
        return userInfoService.saveUserInfo(userInfoDo);
    }

    /**
     * 通过accessToken获取用户信息
     *
     * @param accessToken
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private JSONObject getUserInfo(String accessToken) {
        String api = "https://api.github.com/user";
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api))
                .header("Accept", "application/json")
                .header("Authorization", "token " + accessToken)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return JSONObject.parseObject(response.body());
            } else {
                log.info("Get user info from github error!");
                log.error("Status code: {}", response.statusCode());
                log.error("\n Body: " + response.body());
                return null;
            }
        } catch (InterruptedException e) {
            log.error("getUserInfo exception. msg={}", e.getMessage());
        } catch (IOException ioException) {
            log.error("getUserInfo exception. msg={}", ioException.getMessage());
        }

        return null;
    }

    /**
     * 通过code获取accessToken
     *
     * @param data
     * @throws Exception
     */
    // https://openjdk.org/groups/net/httpclient/recipes.html#post
    private String getAccessToken(Map<String, Object> data) {
        String api = "https://github.com/login/oauth/access_token";
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api))
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofForm(data))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject jsonObject = JSONObject.parseObject(response.body());
                return jsonObject.getString("access_token");
            } else {
                log.info("Get accessToken info from github error!");
                log.error("Get accessToken Status code: {}", response.statusCode());
                log.error("Res Body: {}", response.body());
                return null;
            }
        } catch (InterruptedException e) {
            log.error("getAccessToken exception. msg={}", e.getMessage());
        } catch (IOException ioException) {
            log.error("getAccessToken exception. msg={}", ioException.getMessage());
        }
        return null;
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
