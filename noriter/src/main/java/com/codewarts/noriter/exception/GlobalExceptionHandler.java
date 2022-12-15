package com.codewarts.noriter.exception;

import com.codewarts.noriter.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalNoriterException.class)
    public ResponseEntity<ErrorResponse> globalExceptionHandler(GlobalNoriterException exception) {
        ErrorResponse response = new ErrorResponse(exception);
        return ResponseEntity.status(exception.getStatus()).body(response);
    }

//    @ExceptionHandler(WebClientResponseException.class)
//    public ResponseEntity<ErrorResponse> handler(Exception e) {
//        String message = getMessage(e);
//        log.info("message={}", message);
//        return null;
//    }
//
//    private String getMessage(Exception e) {
//        return e.getMessage();
//    }
}
