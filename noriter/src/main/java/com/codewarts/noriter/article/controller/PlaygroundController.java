package com.codewarts.noriter.article.controller;


import com.codewarts.noriter.article.dto.article.ArticleListResponse;
import com.codewarts.noriter.article.dto.playground.PlaygroundCreateRequest;
import com.codewarts.noriter.article.dto.playground.PlaygroundDetailResponse;
import com.codewarts.noriter.article.dto.playground.PlaygroundUpdateRequest;
import com.codewarts.noriter.article.service.PlaygroundService;
import com.codewarts.noriter.auth.LoginCheck;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/community/playground")
@Validated
public class PlaygroundController {

    private final PlaygroundService playgroundService;

    @PostMapping
    public Long register(@RequestBody @Valid PlaygroundCreateRequest playgroundCreateRequest,
        @LoginCheck Long memberId) {
        return playgroundService.create(playgroundCreateRequest, memberId);
    }

    @GetMapping("/{id}")
    public PlaygroundDetailResponse getDetail(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id, @LoginCheck Long memberId) {
        return playgroundService.findDetail(id, memberId);
    }

    @GetMapping
    public List<ArticleListResponse> getList(@LoginCheck Long memberId) {
        return playgroundService.findList(memberId);
    }

    @PutMapping("/{id}")
    public void edit(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id,
        @RequestBody @Valid PlaygroundUpdateRequest playgroundUpdateRequest,
        @LoginCheck Long memberId) {
        playgroundService.update(id, playgroundUpdateRequest, memberId);
    }

    @DeleteMapping("/{id}")
    public void remove(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id, @LoginCheck Long memberId) {
        playgroundService.delete(id, memberId);
    }
}
