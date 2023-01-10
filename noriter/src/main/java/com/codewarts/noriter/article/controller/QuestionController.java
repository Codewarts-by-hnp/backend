package com.codewarts.noriter.article.controller;

import com.codewarts.noriter.article.domain.dto.question.QuestionDetailResponse;
import com.codewarts.noriter.article.domain.dto.question.QuestionListResponse;
import com.codewarts.noriter.article.domain.dto.question.QuestionPostRequest;
import com.codewarts.noriter.article.domain.dto.question.QuestionUpdateRequest;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.article.service.QuestionService;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.CommonExceptionType;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
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
    public Long register(@RequestBody @Valid QuestionPostRequest postRequest,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        return questionService.create(postRequest, memberId);
    }

    @GetMapping
    public List<QuestionListResponse> questionList(@RequestParam Map<String, String> paramMap,
        HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (paramMap.isEmpty()) {
            return questionService.findList(null, accessToken);
        }
        if (paramMap.size() == 1 && paramMap.containsKey("status")) {
            StatusType status = conversionService.convert(paramMap.get("status"), StatusType.class);
            return questionService.findList(status, accessToken);
        }
        throw new GlobalNoriterException(CommonExceptionType.INCORRECT_REQUEST_PARAM);
    }

    @GetMapping("/{id}")
    public QuestionDetailResponse questionDetail(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id, HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return questionService.findDetail(id, accessToken);
    }

    @DeleteMapping("/{id}")
    public void questionRemove(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        questionService.delete(id, memberId);
    }

    @PutMapping("/{id}")
    public void questionEdit(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id,
        @RequestBody @Valid QuestionUpdateRequest updateRequest,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        questionService.update(id, memberId, updateRequest);
    }

    @PatchMapping("/{id}")
    public void questionChangeStatus(
        @PathVariable(required = false)
        @NotNull(message = "ID가 비어있습니다.")
        @Positive(message = "게시글 ID는 양수이어야 합니다.") Long id,
        @RequestBody Map<String, String> map,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        if (!map.containsKey("status")) {
            throw new GlobalNoriterException(CommonExceptionType.INVALID_REQUEST);
        }
        StatusType status = conversionService.convert(map.get("status"), StatusType.class);
        questionService.updateStatus(id, memberId, status);
    }

    private Long getMemberId(HttpServletRequest request) {
        return (Long) request.getAttribute("memberId");
    }
}
