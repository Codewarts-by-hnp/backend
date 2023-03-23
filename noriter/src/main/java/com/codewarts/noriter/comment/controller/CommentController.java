package com.codewarts.noriter.comment.controller;

import com.codewarts.noriter.auth.LoginCheck;
import com.codewarts.noriter.comment.dto.comment.CommentCreateRequest;
import com.codewarts.noriter.comment.dto.comment.CommentUpdateRequest;
import com.codewarts.noriter.comment.service.CommentService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/{articleId}/comment")
@Validated
@RestController
public class CommentController {

    private final CommentService commentService;
    @PostMapping
    public void registerComment(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long articleId,
        @RequestBody @Valid CommentCreateRequest commentRequest, @LoginCheck Long memberId) {
        commentService.createComment(memberId, articleId, commentRequest);
    }

    @PutMapping("/{id}")
    public void editComment(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long articleId,
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "댓글 ID는 양수이어야 합니다.") Long id,
        @RequestBody @Valid CommentUpdateRequest commentRequest, @LoginCheck Long memberId) {
        commentService.updateComment(memberId, articleId, id, commentRequest);
    }

    @DeleteMapping("/{id}")
    public void removeComment(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long articleId,
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "댓글 ID는 양수이어야 합니다.") Long id, @LoginCheck Long memberId) {
        commentService.deleteComment(memberId, articleId, id);
    }

    @PostMapping("/{id}/recomment")
    public void registerReComment(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long articleId,
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "댓글 ID는 양수이어야 합니다.") Long id,
        @RequestBody @Valid CommentCreateRequest commentCreateRequest,
        @LoginCheck Long memberId) {
        commentService.createReComment(id, articleId, memberId, commentCreateRequest);
    }
}
