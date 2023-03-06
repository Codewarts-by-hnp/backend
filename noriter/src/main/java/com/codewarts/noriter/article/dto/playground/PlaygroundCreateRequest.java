package com.codewarts.noriter.article.dto.playground;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.article.dto.article.ArticleCreateRequest;
import com.codewarts.noriter.member.domain.Member;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlaygroundCreateRequest extends ArticleCreateRequest {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    private List<String> hashtags;

    @Override
    public Article toEntity(Member writer) {
        Article article = Article.builder()
            .title(title)
            .content(content)
            .writer(writer)
            .articleType(ArticleType.PLAYGROUND)
            .build();
        article.addHashtags(hashtags);
        return article;
    }
}

