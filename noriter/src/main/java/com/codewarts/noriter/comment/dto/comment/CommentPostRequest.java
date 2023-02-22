package com.codewarts.noriter.comment.dto.comment;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.comment.domain.Comment;
import com.codewarts.noriter.member.domain.Member;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentPostRequest {

    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    @NotNull(message = "내용은 필수입니다.")
    private Boolean secret;

    public Comment toEntity(Article article, Member writer) {
        return Comment.builder()
            .article(article)
            .writer(writer)
            .content(content)
            .secret(secret)
            .writtenTime(LocalDateTime.now())
            .editedTime(LocalDateTime.now())
            .build();
    }
}
