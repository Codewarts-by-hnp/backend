package com.codewarts.noriter.exception.type;

import org.springframework.http.HttpStatus;

public interface ExceptionType {

    String getErrorCode();
    String getErrorMessage();
    HttpStatus getStatus();
}
