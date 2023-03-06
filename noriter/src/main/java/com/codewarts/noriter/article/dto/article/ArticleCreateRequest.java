package com.codewarts.noriter.article.dto.article;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.member.domain.Member;
import java.util.List;

public abstract class ArticleCreateRequest {

    private String title;
    private String content;
    private List<String> hashtags;

    public abstract Article toEntity(Member writer);
}
