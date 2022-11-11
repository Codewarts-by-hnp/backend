package com.codewarts.noriter.article.controller;

import com.codewarts.noriter.article.domain.dto.question.QuestionPostRequest;
import com.codewarts.noriter.article.service.QuestionService;
import com.codewarts.noriter.auth.jwt.JwtProvider;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/community")
public class QuestionController {

    private final QuestionService questionService;
    private final JwtProvider jwtProvider;

    @PostMapping("/question")
    public void create(@RequestBody QuestionPostRequest questionRequest, HttpServletRequest request) {
        Long memberId = jwtProvider.decode(request.getHeader("Authorization"));
        questionService.add(questionRequest, memberId);
    }
}
