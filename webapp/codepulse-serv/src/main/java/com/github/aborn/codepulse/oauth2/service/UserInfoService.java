package com.github.aborn.codepulse.oauth2.service;

import com.github.aborn.codepulse.oauth2.UserInfoMapper;
import com.github.aborn.codepulse.oauth2.datatypes.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author aborn (jiangguobao)
 * @date 2023/07/18 11:53
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoMapper userInfoMapper;

    public void saveUserInfo(UserInfo userInfo) {
        // 先判断是否存在
        UserInfo userInfoExist = userInfoMapper.queryUserByOpenIdAndThirdType(userInfo.getOpenid(), userInfo.getThirdType());
        if (userInfoExist == null) {
            // 当用户不存在时，保存用户信息到DB
        } else {
            // 否则更新用户信息 （前提是不允许用户手工修改）
        }

    }
}
