package com.codewarts.noriter.comment.controller;

import com.codewarts.noriter.comment.dto.recomment.ReCommentRequest;
import com.codewarts.noriter.comment.service.CommentService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/{articleId}/comment")
@Validated
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{commentId}/recomment")
    public void registerReComment(@PathVariable(required = false)
    @NotNull(message = "ID가 비어있습니다.")
    @Positive(message = "게시글 ID는 양수이어야 합니다.") Long articleId,
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "댓글 ID는 양수이어야 합니다.") Long commentId,
        @RequestBody @Valid ReCommentRequest reCommentRequest,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);

        commentService.createReComment(articleId, commentId, memberId, reCommentRequest);
    }

    private Long getMemberId(HttpServletRequest request) {
        return (Long) request.getAttribute("memberId");
    }

}
