package com.github.aborn.codepulse.api;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author aborn (jiangguobao)
 * @date 2023/02/10 13:15
 */
@Mapper
public interface CodePulseMapper {
    @Select("SELECT * FROM cp_user_coding_daily WHERE id=#{id}")
    CodePulseInfo findById(long id);

    @Select("SELECT * FROM cp_user_coding_daily WHERE token=#{token} AND day=#{day} limit 1")
    CodePulseInfo findByTokenAndDay(String token, String day);

    @Insert("INSERT INTO cp_user_coding_daily (`id`,`uid`,token,day,`code_info`,create_time,update_time,create_by,update_by) "
                    + "VALUES(#{id},#{uid},#{token},#{day},#{codeInfo},#{createTime},#{updateTime},#{createBy},#{updateBy})")
    void insert(CodePulseInfo pulseInfo);

    @Update("<script>UPDATE cp_user_coding_daily SET <if test='uid!=null'>`uid`=#{uid},</if>"
                    + "<if test='codeInfo!=null'>codeInfo=#{codeInfo},</if>"
                    + "update_date=#{updateTime} WHERE id=#{id}</script>")
    void update(CodePulseInfo flow);
}
