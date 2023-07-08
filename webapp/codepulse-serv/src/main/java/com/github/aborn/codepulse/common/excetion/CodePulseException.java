package com.github.aborn.codepulse.common.excetion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.lang.reflect.InvocationTargetException;

@Getter
@Setter
@NoArgsConstructor
public class CodePulseException extends RuntimeException {
    private String error;
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private String message;

    public CodePulseException(String message) {
        this.message = message;
    }

    public CodePulseException(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public CodePulseException(HttpStatus httpStatus, String error, String message) {
        this.httpStatus = httpStatus;
        this.error = error;
        this.message = message;
    }

    public static CodePulseException wrap(Throwable e, String message) {
        if (e instanceof InvocationTargetException) {
            e = ((InvocationTargetException) e).getTargetException();
        }
        if (e instanceof CodePulseException) {
            return (CodePulseException) e;
        }
        return new CodePulseException(message);
    }

    @Override
    public String getMessage() {
        if (message == null) {
            return super.getMessage();
        }
        return message;
    }

    public CodePulseException setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getStatus() {
        return this.httpStatus.value();
    }
}
