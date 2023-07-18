package com.github.aborn.codepulse.oauth2.service;

import com.github.aborn.codepulse.oauth2.UserInfoMapper;
import com.github.aborn.codepulse.oauth2.UserTokenManager;
import com.github.aborn.codepulse.oauth2.datatypes.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author aborn (jiangguobao)
 * @date 2023/07/18 11:53
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoMapper userInfoMapper;

    public UserInfo saveUserInfo(UserInfo userInfo) {
        // 先判断是否存在
        UserInfo userInfoExist = userInfoMapper.queryUserByOpenIdAndThirdType(userInfo.getOpenid(), userInfo.getThirdType());
        if (userInfoExist == null) {
            // 当用户不存在时，创建一个用户，并保存用户信息到DB
            String token = UserTokenManager.generateToken(userInfo.getUid());
            UserInfo u = userInfoMapper.queryUserByToken(token);
            if (u != null) {
                // token重复，重新生成token
                log.error("Token repeated, regenerate it! {}", token);
                token = UserTokenManager.generateToken(userInfo.getUid(), userInfo.getOpenid());
            }
            userInfo.setToken(token);
            userInfoMapper.insert(userInfo);
            return userInfoMapper.queryUserByToken(token);
        } else {
            // 否则更新用户信息 （前提是不允许用户手工修改）
            userInfoExist.setAvatar(userInfo.getAvatar());
            userInfoExist.setUid(userInfo.getUid());
            userInfoExist.setName(userInfo.getName());
            userInfoExist.setUpdateTime(new Date());
            userInfoExist.setUpdateBy(userInfo.getName());
            userInfoMapper.update(userInfo);
            return userInfoExist;
        }
    }
}
