package com.codewarts.noriter.article.domain.dto.question;

import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.member.domain.Member;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPostRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    private List<String> hashtag;

    public Question toEntity(Member writer) {
        return Question.builder()
            .title(title)
            .content(content)
            .writer(writer)
            .writtenTime(LocalDateTime.now())
            .editedTime(LocalDateTime.now())
            .articleType(ArticleType.QUESTION)
            .status(StatusType.INCOMPLETE)
            .build();
    }
}
