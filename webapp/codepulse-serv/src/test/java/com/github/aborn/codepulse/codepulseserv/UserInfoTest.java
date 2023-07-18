package com.github.aborn.codepulse.codepulseserv;

import com.github.aborn.codepulse.oauth2.UserInfoMapper;
import com.github.aborn.codepulse.oauth2.datatypes.UserInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author aborn (jiangguobao)
 * @date 2023/07/18 14:43
 */
public class UserInfoTest extends CodepulseBaseTest {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    public void testUserInfo() {
        try {
            UserInfo userInfo = userInfoMapper.queryUserByOpenIdAndThirdType("ss", 0);
            System.out.println(userInfo);
        } catch (Exception e) {

        }
    }

}
