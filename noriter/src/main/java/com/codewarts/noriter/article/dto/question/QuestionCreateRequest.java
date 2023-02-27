package com.codewarts.noriter.article.dto.question;

import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.member.domain.Member;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    private List<String> hashtags;

    public Question toEntity(Member writer) {
        Question question = Question.builder()
            .title(title)
            .content(content)
            .writer(writer)
            .articleType(ArticleType.QUESTION)
            .status(StatusType.INCOMPLETE)
            .build();
        question.addHashtags(hashtags);
        return question;
    }
}
