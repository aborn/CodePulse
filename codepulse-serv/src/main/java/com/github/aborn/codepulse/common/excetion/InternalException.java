package com.github.aborn.codepulse.common.excetion;

import org.springframework.http.HttpStatus;

public class InternalException extends CodePulseException {

    public InternalException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "internal_error", message);
    }
}
