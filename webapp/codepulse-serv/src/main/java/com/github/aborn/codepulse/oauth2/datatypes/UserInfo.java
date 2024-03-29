package com.github.aborn.codepulse.oauth2.datatypes;

import com.alibaba.fastjson2.JSONObject;
import com.github.aborn.codepulse.common.datatypes.BaseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author aborn (jiangguobao)
 * @date 2023/07/18 10:14
 */
@Data
@NoArgsConstructor
public class UserInfo extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 14996675877480615L;

    private long id;

    private String token;

    private String openid;

    private int thirdType;

    private String avatar;

    private String uid;

    private String name;

    private String team = "";

    private String corp = "";

    public UserInfo(JSONObject userInfo) {
        this.openid = String.valueOf(userInfo.getInteger("id"));
        this.avatar = userInfo.getString("avatar_url");
        this.uid = userInfo.getString("login");  // aborn
        this.name = userInfo.getString("name");  // Aborn Jiang
        this.thirdType = ThirdType.GITHUB;
        this.createTime = new Date();
        this.updateTime = new Date();
    }

}
