package com.codewarts.noriter.exception.type;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthExceptionType implements ExceptionType {

    RESOURCE_SERVER_NOT_FOUND("AUTH001", "존재하지 않는 Resource Server 입니다.", HttpStatus.NOT_FOUND),
    EMPTY_AUTHORIZATION_CODE("AUTH002", "비어있는 code 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_AUTHORIZATION_CODE("AUTH003", "유효하지 않은 code 입니다.", HttpStatus.UNAUTHORIZED),
    EMPTY_ACCESS_TOKEN("AUTH004", "비어있는 Access Token 입니다.", HttpStatus.BAD_REQUEST),
    TAMPERED_ACCESS_TOKEN("AUTH005", "변조된 Access Token 입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_ACCESS_TOKEN("AUTH006", "만료된 Access Token 입니다.", HttpStatus.UNAUTHORIZED);

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
