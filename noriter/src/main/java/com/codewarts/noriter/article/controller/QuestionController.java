package com.codewarts.noriter.article.controller;

import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.article.dto.article.ArticleListResponse;
import com.codewarts.noriter.article.dto.question.QuestionCreateRequest;
import com.codewarts.noriter.article.dto.question.QuestionDetailResponse;
import com.codewarts.noriter.article.dto.question.QuestionUpdateRequest;
import com.codewarts.noriter.article.service.QuestionService;
import com.codewarts.noriter.auth.LoginCheck;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/community/question")
@Validated
public class QuestionController {

    private final QuestionService questionService;
    private final ConversionService conversionService;

    @PostMapping
    public Long register(@RequestBody @Valid QuestionCreateRequest postRequest,
        @LoginCheck Long memberId) {
        return questionService.create(postRequest, memberId);
    }

    @GetMapping
    public List<ArticleListResponse> getList(@RequestParam Map<String, String> paramMap,
        @LoginCheck Long memberId) {
        if (paramMap.isEmpty()) {
            return questionService.findList(null, memberId);
        }
        if (paramMap.size() != 1 || !paramMap.containsKey("status")) {
            throw new GlobalNoriterException(CommonExceptionType.INCORRECT_REQUEST_PARAM);
        }

        StatusType status = conversionService.convert(paramMap.get("status"), StatusType.class);
        return questionService.findList(status, memberId);
    }

    @GetMapping("/{id}")
    public QuestionDetailResponse getDetail(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id, @LoginCheck Long memberId) {
        return questionService.findDetail(id, memberId);
    }

    @DeleteMapping("/{id}")
    public void remove(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id, @LoginCheck Long memberId) {
        questionService.delete(id, memberId);
    }

    @PutMapping("/{id}")
    public void edit(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id,
        @RequestBody @Valid QuestionUpdateRequest updateRequest, @LoginCheck Long memberId) {
        questionService.update(id, updateRequest, memberId);
    }

    @PatchMapping("/{id}")
    public void changeStatus(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id,
        @RequestBody Map<String, String> map, @LoginCheck Long memberId) {
        if (!map.containsKey("status")) {
            throw new GlobalNoriterException(CommonExceptionType.INVALID_REQUEST);
        }
        StatusType status = conversionService.convert(map.get("status"), StatusType.class);
        questionService.updateStatus(id, memberId, status);
    }
}
