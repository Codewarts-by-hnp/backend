package com.codewarts.noriter.comment.dto.comment;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.comment.domain.Comment;
import com.codewarts.noriter.member.domain.Member;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {

    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    @NotNull(message = "내용은 필수입니다.")
    private Boolean secret;
    private String dummy;
    public Comment toComment(Article article, Member writer) {
        return Comment.builder()
            .article(article)
            .writer(writer)
            .content(content)
            .secret(secret)
            .build();
    }

    public Comment toRecomment(Article article, Member writer, Comment comment) {
        return Comment.builder()
            .article(article)
            .writer(writer)
            .parent(comment)
            .content(content)
            .secret(secret)
            .build();
    }

}
