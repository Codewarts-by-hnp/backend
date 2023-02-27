package com.codewarts.noriter.comment.dto.recomment;

import com.codewarts.noriter.comment.domain.Comment;
import com.codewarts.noriter.comment.domain.ReComment;
import com.codewarts.noriter.member.domain.Member;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReCommentCreateRequest {

    @NotBlank(message = "내용은 필수입니다.")
    private String  content;
    @NotNull(message = "내용은 필수입니다.")
    private Boolean secret;

    public  ReComment toEntity(Comment comment, Member member) {
        return new ReComment(comment, member, content, secret);
    }
}
