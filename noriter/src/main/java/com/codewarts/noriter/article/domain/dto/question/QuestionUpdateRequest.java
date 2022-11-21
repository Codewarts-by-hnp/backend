package com.codewarts.noriter.article.domain.dto.question;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateRequest {

    private String title;
    private String content;
    private List<String> hashtag;
}
