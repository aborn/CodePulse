package com.github.aborn.codepulse.oauth2;

import com.github.aborn.codepulse.admin.datatypes.UserActionResponse;
import com.github.aborn.codepulse.common.datatypes.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping(value = "login")
    @ResponseBody
    public BaseResponse<Object> getUserAction(@NonNull String code, String state) {
        /**
         * client_id: string
         *     client_secret: string
         *     code: string
         *     redirect_uri: string
         */
        return BaseResponse.success("good");
    }
}
