package com.codewarts.noriter.article.controller;

import com.codewarts.noriter.article.domain.dto.study.StudyPostRequest;
import com.codewarts.noriter.article.service.StudyService;
import com.codewarts.noriter.auth.jwt.JwtProvider;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/gathering")
public class StudyController {

    private final StudyService studyService;
    private final JwtProvider jwtProvider;

    @PostMapping
    public void register(@RequestBody StudyPostRequest studyPostRequest,
        HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        Long memberId = jwtProvider.decode(authorization);
        studyService.register(studyPostRequest, memberId);
    }

}
