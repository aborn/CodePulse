package com.github.aborn.codepulse.api;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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

    @Insert("INSERT INTO cp_user_coding_daily (`id`,token,day,`code_info`,create_time,update_time,create_by,update_by,code_time) "
            + "VALUES(#{id},#{token},#{day},#{codeInfo},#{createTime},#{updateTime},#{createBy},#{updateBy},#{codeTime})")
    void insert(CodePulseInfo pulseInfo);

    @Update("<script>UPDATE cp_user_coding_daily SET <if test='token!=null'>`token`=#{token},</if>"
            + "<if test='codeInfo!=null'>code_info=#{codeInfo},</if>"
            + "code_time=#{codeTime},"
            + "update_time=#{updateTime} WHERE id=#{id}</script>")
    void update(CodePulseInfo pulseInfo);

    @Select(
            "<script>SELECT * FROM cp_user_coding_daily WHERE token=#{token} AND day IN "
                    + "<foreach collection='days' item='day' open='(' separator=',' close=')'>#{day}</foreach></script>")
    List<CodePulseInfo> queryList(String token, List<String> days);

    @Select(
            "<script>SELECT * FROM cp_user_coding_daily WHERE day=#{day} order by code_time DESC limit 10</script>")
    List<CodePulseInfo> queryDailyTrending(String day);
}
