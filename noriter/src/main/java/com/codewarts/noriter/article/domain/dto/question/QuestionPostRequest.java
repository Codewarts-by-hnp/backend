package com.codewarts.noriter.article.domain.dto.question;

import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.common.domain.Member;
import java.time.LocalDateTime;
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
            .writtenTime(LocalDateTime.now())
            .editedTime(LocalDateTime.now())
            .articleType(ArticleType.QUESTION)
            .build();
    }

//    public Question toEntity(Member writer) {
//        return Question.builder()
//            .title(title)
//            .content(content)
//            .writer(member)
//            .build();
//    }
}
