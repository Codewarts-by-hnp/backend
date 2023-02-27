package com.codewarts.noriter.article.dto.study;

import com.codewarts.noriter.article.domain.Study;
import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.member.domain.Member;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GatheringCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    private List<String> hashtags;

    public Study toEntity(Member writer) {
        return Study.builder()
            .title(title)
            .content(content)
            .writer(writer)
            .writtenTime(LocalDateTime.now())
            .editedTime(LocalDateTime.now())
            .articleType(ArticleType.STUDY)
            .status(StatusType.INCOMPLETE)
            .build();
    }
}
