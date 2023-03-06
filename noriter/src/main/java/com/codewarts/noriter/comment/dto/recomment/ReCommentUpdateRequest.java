package com.codewarts.noriter.comment.dto.recomment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReCommentUpdateRequest {

    @NotBlank(message = "내용은 필수입니다.")
    private String  content;
    @NotNull(message = "내용은 필수입니다.")
    private Boolean secret;
}
