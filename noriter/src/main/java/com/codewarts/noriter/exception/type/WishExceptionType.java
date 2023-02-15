package com.codewarts.noriter.exception.type;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum WishExceptionType implements ExceptionType {

    WISH_ALREADY_EXIST("WISH001", "이미 찜한 게시글은 찜할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_CANCEL_NOT_EXIST_WISH("WISH002", "찜하지 않은 게시글은 취소할 수 없습니다.", HttpStatus.BAD_REQUEST);

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
