package com.github.aborn.codepulse.oauth2;

import com.github.aborn.codepulse.oauth2.datatypes.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Insert("INSERT INTO cp_user_info (`id`,token,openid,`third_type`,avatar, uid, name, team, corp,create_time,update_time,create_by,update_by) "
            + "VALUES(#{id},#{token},#{openid},#{thirdType}, #{avatar}, #{uid}, #{name}, #{team}, #{corp},#{createTime},#{updateTime},#{createBy},#{updateBy})")
    void insert(UserInfo userInfo);

    @Update("<script>UPDATE cp_user_info SET <if test='avatar!=null'>`avatar`=#{avatar},</if>"
            + "<if test='uid!=null'>uid=#{uid},</if>"
            + "<if test='name!=null'>uid=#{name},</if>"
            + "<if test='team!=null'>uid=#{team},</if>"
            + "<if test='corp!=null'>uid=#{corp},</if>"
            + "update_time=#{updateTime} WHERE id=#{id}</script>")
    void update(UserInfo userInfo);
}
