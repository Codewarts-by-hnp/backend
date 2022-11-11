package com.codewarts.noriter.article.domain.dto.question;

import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.common.domain.Member;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPostRequest {

    private String title;
    private String content;
    private List<String> hashtag;

    public Question toEntity(Member writer) {
        return Question.builder()
            .title(title)
            .content(content)
            .writer(writer)
            .build();
    }
}
