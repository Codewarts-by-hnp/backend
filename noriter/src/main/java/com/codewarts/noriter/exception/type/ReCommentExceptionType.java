package com.codewarts.noriter.exception.type;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ReCommentExceptionType implements ExceptionType{

    RECOMMENT_NOT_FOUND("RECOMMENT001", "존재하지 않는 대댓글입니다.", HttpStatus.NOT_FOUND),
    RECOMMENT_NOT_MATCHED_WRITER("RECOMMENT002", "작성자만이 편집할 수 있습니다.", HttpStatus.UNAUTHORIZED),
    DELETED_RECOMMENT("RECOMMENT003", "삭제된 대댓글입니다.", HttpStatus.NOT_FOUND),
    NOT_MATCHED_COMMENT("RECOMMENT004", "해당 댓글의 대댓글이 아닙니다.", HttpStatus.BAD_REQUEST);

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
