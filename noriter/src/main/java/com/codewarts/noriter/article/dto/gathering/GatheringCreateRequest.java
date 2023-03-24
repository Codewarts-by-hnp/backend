package com.codewarts.noriter.article.dto.gathering;

import com.codewarts.noriter.article.domain.Gathering;
import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.article.dto.article.ArticleCreateRequest;
import com.codewarts.noriter.member.domain.Member;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GatheringCreateRequest extends ArticleCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    private List<String> hashtags;

    @Override
    public Gathering toEntity(Member writer) {
        Gathering gathering = Gathering.builder()
            .title(title)
            .content(content)
            .writer(writer)
            .articleType(ArticleType.GATHERING)
            .status(StatusType.INCOMPLETE)
            .build();
        gathering.addHashtags(hashtags);
        return gathering;
    }
}
