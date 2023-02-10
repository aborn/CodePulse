package com.github.aborn.codepulse.api;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author aborn (jiangguobao)
 * @date 2023/02/10 13:15
 */
@Mapper
public interface CodePulseMapper {
    @Select("SELECT * FROM cp_user_coding_daily WHERE id=#{id}")
    CodePulseInfo findById(long id);
}
