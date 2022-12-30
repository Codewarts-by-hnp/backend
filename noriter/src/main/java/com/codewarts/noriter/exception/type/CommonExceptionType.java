package com.codewarts.noriter.exception.type;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommonExceptionType implements ExceptionType {


    INVALID_REQUEST("INVALID001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    INCORRECT_REQUEST_PARAM("INVALID002", "올바르지 않은 requestParam입니다.", HttpStatus.BAD_REQUEST),
    INCORRECT_REQUEST_PARAM_TYPE("INVALID003", "올바르지 않은 requestParam 타입입니다.", HttpStatus.BAD_REQUEST);
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
