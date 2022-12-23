package com.codewarts.noriter.exception.type;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ArticleExceptionType implements ExceptionType {
    ARTICLE_NOT_FOUND("ARTICLE001", "존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),
    ARTICLE_NOT_MATCHED_WRITER("ARTICLE002", "작성자만이 편집할 수 있습니다.", HttpStatus.NOT_FOUND);

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
