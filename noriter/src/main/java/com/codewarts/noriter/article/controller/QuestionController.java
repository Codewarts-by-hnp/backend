package com.codewarts.noriter.article.controller;

import com.codewarts.noriter.article.domain.dto.question.QuestionDetailResponse;
import com.codewarts.noriter.article.domain.dto.question.QuestionPostRequest;
import com.codewarts.noriter.article.domain.dto.question.QuestionResponse;
import com.codewarts.noriter.article.domain.dto.question.QuestionUpdateRequest;
import com.codewarts.noriter.article.service.QuestionService;
import com.codewarts.noriter.auth.jwt.JwtProvider;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
public class QuestionController {

    private final QuestionService questionService;
    private final JwtProvider jwtProvider;

    @PostMapping
    public void create(@RequestBody QuestionPostRequest postRequest,
        HttpServletRequest request) {
        Long memberId = jwtProvider.decode(request.getHeader("Authorization"));
        questionService.add(postRequest, memberId);
    }

    @GetMapping
    public List<QuestionResponse> questionList(@RequestParam(value = "completion", required = false) Boolean status) {
        return questionService.findList(status);
    }

    @GetMapping("/{id}")
    public QuestionDetailResponse questionDetail(@PathVariable Long id) {
        return questionService.findDetail(id);
    }

    @DeleteMapping("/{id}")
    public void questionRemove(@PathVariable Long id, HttpServletRequest request) {
        Long memberId = jwtProvider.decode(request.getHeader("Authorization"));
        questionService.delete(id, memberId);
    }

    @PutMapping("/{id}")
    public void questionEdit(@PathVariable Long id, @RequestBody QuestionUpdateRequest updateRequest, HttpServletRequest request) {
        Long memberId = jwtProvider.decode(request.getHeader("Authorization"));
        questionService.update(id, memberId, updateRequest);
    }

    @PatchMapping("/{id}")
    public void questionEditCompleted(@PathVariable Long id, boolean completed, HttpServletRequest request) {
        Long memberId = jwtProvider.decode(request.getHeader("Authorization"));
        questionService.updateComplete(id, memberId, completed);
    }
}
