package com.codewarts.noriter.comment.domain.dto;

import com.codewarts.noriter.comment.domain.Comment;
import com.codewarts.noriter.common.domain.dto.WriterInfoResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponse {

    private Long id;
    private String content;
    private WriterInfoResponse writer;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writer = new WriterInfoResponse(comment.getWriter());
    }
}
