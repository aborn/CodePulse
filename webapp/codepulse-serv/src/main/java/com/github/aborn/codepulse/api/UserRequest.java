package com.github.aborn.codepulse.api;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.io.Serializable;

/**
 * @author aborn (jiangguobao)
 * @date 2024/05/07 14:27
 */
@Data
public class UserRequest implements Serializable {
    public UserRequest() {
    }

    @NonNull
    String token;

    @NonNull String day;
}
