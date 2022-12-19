package com.codewarts.noriter.exception.response;

import com.codewarts.noriter.exception.GlobalNoriterException;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String errorCode;
    private final String message;

    public ErrorResponse(GlobalNoriterException e) {
        this.errorCode = e.getErrorCode();
        this.message = e.getMessage();
    }
}
