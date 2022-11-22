package com.codewarts.noriter.article.domain.dto.study;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyEditRequest {

    private String title;
    private String content;
    private List<String> hashtags;

}
