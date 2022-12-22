package com.codewarts.noriter.exception;

import com.codewarts.noriter.exception.response.ErrorResponse;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import java.util.HashMap;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalNoriterException.class)
    public ResponseEntity<ErrorResponse> globalExceptionHandler(GlobalNoriterException exception) {
        ErrorResponse response = new ErrorResponse(exception);
        return ResponseEntity.status(exception.getStatus()).body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidBindHandler(MethodArgumentNotValidException exception) {
        ErrorResponse response = ErrorResponse.builder()
            .errorCode(CommonExceptionType.INVALID_REQUEST.getErrorCode())
            .message(CommonExceptionType.INVALID_REQUEST.getErrorMessage())
            .detail(new HashMap<>())
            .build();

        for (FieldError fieldError : exception.getFieldErrors()) {
            response.addDetail(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse invalidRequestHandler(ConstraintViolationException exception) {
        return ErrorResponse.builder()
            .errorCode(CommonExceptionType.INVALID_REQUEST.getErrorCode())
            .message(exception.getMessage())
            .build();
    }
}
