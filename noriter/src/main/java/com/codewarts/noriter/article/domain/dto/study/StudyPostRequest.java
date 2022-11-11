package com.codewarts.noriter.article.domain.dto.study;

import com.codewarts.noriter.article.domain.Study;
import com.codewarts.noriter.common.domain.Member;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyPostRequest {

    private String title;
    private String content;
    private List<String> hashtags;


    public Study toEntity(Member member) {
        return Study.builder()
            .title(title)
            .content(content)
            .writer(member)
            .build();
    }
}
