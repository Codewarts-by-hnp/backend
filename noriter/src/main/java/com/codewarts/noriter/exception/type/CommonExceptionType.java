package com.codewarts.noriter.exception.type;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommonExceptionType implements ExceptionType {


    INVALID_REQUEST("INVALID001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;


    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getStatus() {
        return this.httpStatus;
    }
}
