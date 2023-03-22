package com.codewarts.noriter.exception.type;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommentExceptionType implements ExceptionType {

    COMMENT_NOT_FOUND("COMMENT001", "존재하지 않는 댓글입니다.", HttpStatus.NOT_FOUND),
    DELETED_COMMENT("COMMENT002", "삭제된 댓글입니다.", HttpStatus.BAD_REQUEST),
    NOT_MATCHED_WRITER("COMMENT003", "작성자만이 편집할 수 있습니다.", HttpStatus.UNAUTHORIZED),
    NOT_MATCHED_ARTICLE("COMMENT004", "해당 게시글의 댓글이 아닙니다.", HttpStatus.BAD_REQUEST),

    NOT_ALLOWED_RECOMMENT("COMMENT005", "대댓글에 대댓글을 생성할 수 없습니다.", HttpStatus.BAD_REQUEST);


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
