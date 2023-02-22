package com.codewarts.noriter.comment.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequest {

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @NotNull(message = "내용은 필수입니다.")
    private Boolean secret;
}
