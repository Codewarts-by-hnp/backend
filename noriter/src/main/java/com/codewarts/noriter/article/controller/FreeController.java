package com.codewarts.noriter.article.controller;


import com.codewarts.noriter.article.dto.free.FreeDetailResponse;
import com.codewarts.noriter.article.dto.free.FreeEditRequest;
import com.codewarts.noriter.article.dto.free.FreeListResponse;
import com.codewarts.noriter.article.dto.free.FreePostRequest;
import com.codewarts.noriter.article.service.FreeService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
public class FreeController {

    private final FreeService freeService;

    @PostMapping
    public Long register(@RequestBody @Valid FreePostRequest freePostRequest,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        return freeService.create(freePostRequest, memberId);
    }

    @GetMapping("/{id}")
    public FreeDetailResponse freeDetail(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id, HttpServletRequest request) {
        Long memberId = getMemberId(request);
        return freeService.findDetail(id, memberId);
    }

    @GetMapping
    public List<FreeListResponse> freeList(HttpServletRequest request) {
        Long memberId = getMemberId(request);
        return freeService.findList(memberId);
    }

    @PutMapping("/{id}")
    public void freeEdit(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id,
        @RequestBody @Valid FreeEditRequest freeEditRequest,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        freeService.update(id, freeEditRequest, memberId);
    }

    @DeleteMapping("/{id}")
    public void freeRemove(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        freeService.delete(id, memberId);
    }

    private Long getMemberId(HttpServletRequest request) {
        return (Long) request.getAttribute("memberId");
    }
}
