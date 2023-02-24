package com.codewarts.noriter.comment.dto.comment;

import com.codewarts.noriter.comment.domain.Comment;
import com.codewarts.noriter.comment.dto.recomment.RecommentResponse;
import com.codewarts.noriter.member.dto.WriterInfoResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class CommentResponse {

    private final Long id;
    private final String content;
    private final WriterInfoResponse writer;
    private final List<RecommentResponse> recomments;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writer = WriterInfoResponse.from(comment.getWriter());
        this.recomments = comment.getReComments().stream()
            .map(RecommentResponse::new)
            .collect(Collectors.toList());
    }
}
