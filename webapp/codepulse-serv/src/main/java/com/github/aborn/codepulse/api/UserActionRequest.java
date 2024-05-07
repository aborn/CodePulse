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

}
