package com.codewarts.noriter.article.dto.article;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class ArticleUpdateRequest {

    private String title;
    private String content;
    private List<String> hashtags;
}
