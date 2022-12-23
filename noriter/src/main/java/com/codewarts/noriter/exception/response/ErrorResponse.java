package com.codewarts.noriter.exception.response;

import com.codewarts.noriter.exception.GlobalNoriterException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(value = Include.NON_EMPTY)
public class ErrorResponse {

    private final String errorCode;
    private final String message;

    private final Map<String, String> detail;

    public ErrorResponse(GlobalNoriterException e) {
        this.errorCode = e.getErrorCode();
        this.message = e.getMessage();
        this.detail = new HashMap<>();
    }

    @Builder
    public ErrorResponse(String errorCode, String message, Map<String, String> detail) {
        this.errorCode = errorCode;
        this.message = message;
        this.detail = detail;
    }

    public void addDetail(String fieldName, String detailMessage) {
        this.detail.put(fieldName, detailMessage);
    }
}
