package com.codewarts.noriter.article.domain.dto.free;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FreeEditRequest {

    private String title;
    private String content;
    private List<String> hashtags;

}
