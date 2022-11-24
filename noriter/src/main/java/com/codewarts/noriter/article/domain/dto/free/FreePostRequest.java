package com.codewarts.noriter.article.domain.dto.free;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.common.domain.Member;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FreePostRequest {

    private String title;
    private String content;
    private List<String> hashtags;


    public Article toEntity(Member writer) {
        return Article.builder()
            .title(title)
            .content(content)
            .writer(writer)
            .writtenTime(LocalDateTime.now())
            .editedTime(LocalDateTime.now())
            .articleType(ArticleType.FREE)
            .build();
    }

}

