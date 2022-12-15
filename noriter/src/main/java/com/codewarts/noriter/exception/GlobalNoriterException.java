package com.codewarts.noriter.exception;

import com.codewarts.noriter.exception.type.ExceptionType;
import org.springframework.http.HttpStatus;

public class GlobalNoriterException extends RuntimeException {

    private final ExceptionType exceptionType;

    public GlobalNoriterException(ExceptionType exceptionType) {
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }

    public String getErrorCode() {
        return exceptionType.getErrorCode();
    }

    public HttpStatus getStatus() {
        return exceptionType.getStatus();
    }
}
