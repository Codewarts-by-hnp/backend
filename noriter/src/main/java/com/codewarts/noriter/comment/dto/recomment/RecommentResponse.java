package com.codewarts.noriter.comment.dto.recomment;

import com.codewarts.noriter.comment.domain.ReComment;
import com.codewarts.noriter.member.dto.WriterInfoResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecommentResponse {

    private final Long id;
    private final String content;
    private final WriterInfoResponse writer;

    public RecommentResponse(ReComment reComment) {
        this.id = reComment.getId();
        this.content = reComment.getContent();
        this.writer = WriterInfoResponse.from(reComment.getWriter());
    }
}
