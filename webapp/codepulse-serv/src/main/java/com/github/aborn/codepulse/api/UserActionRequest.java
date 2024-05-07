package com.github.aborn.codepulse.api;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.io.Serializable;

/**
 * 上报请求的Request
 * @author aborn (jiangguobao)
 * @date 2023/02/10 09:54
 */
@Data
public class UserActionRequest extends UserRequest implements Serializable {
    public UserActionRequest() {
    }

    @NonNull byte[] dayBitSetArray;

    // IDE类型， 0:未知，1:表示 intellij, 2:表示 vscode, 3:表示 visual studio
    int ide = 0;

    // 扩展参数，留着以后用
    String ext;
}
