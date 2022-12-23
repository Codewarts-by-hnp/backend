package com.codewarts.noriter.exception.type;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MemberExceptionType implements ExceptionType {

    MEMBER_NOT_FOUND("MEMBER001", "존재하지 않는 회원입니다.", HttpStatus.UNAUTHORIZED);

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
