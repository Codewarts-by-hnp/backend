package com.codewarts.noriter.comment.domain.dto;

import com.codewarts.noriter.comment.domain.Comment;
import com.codewarts.noriter.member.domain.dto.WriterInfoResponse;
import lombok.Getter;

@Getter
public class CommentResponse {

    private final Long id;
    private final String content;
    private final WriterInfoResponse writer;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writer = WriterInfoResponse.from(comment.getWriter());
    }
}
