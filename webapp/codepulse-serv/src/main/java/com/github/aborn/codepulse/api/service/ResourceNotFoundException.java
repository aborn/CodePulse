package com.github.aborn.codepulse.api.service;

import com.github.aborn.codepulse.common.excetion.CodePulseException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends CodePulseException {

    public ResourceNotFoundException(String type, Object value) {
        super(
                HttpStatus.NOT_FOUND,
                "resource_not_found",
                String.format("Requested %s %s not found", type, value));
    }

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "resource_not_found", message);
    }
}
