package com.github.aborn.codepulse.oauth2;

import com.github.aborn.codepulse.oauth2.datatypes.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author aborn (jiangguobao)
 * @date 2023/07/18 11:52
 */
@Mapper
public interface UserInfoMapper {

    @Select("SELECT * FROM cp_user_info WHERE token=#{token}")
    UserInfo queryUserByToken(String token);

    @Select("SELECT * FROM cp_user_info WHERE openid=#{openid} and third_type=#{thirdType}}")
    UserInfo queryUserByOpenIdAndThirdType(String openid, int thirdType);
}
