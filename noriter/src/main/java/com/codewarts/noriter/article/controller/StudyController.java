package com.codewarts.noriter.article.controller;

import com.codewarts.noriter.article.domain.dto.study.StudyDetailResponse;
import com.codewarts.noriter.article.domain.dto.study.StudyListResponse;
import com.codewarts.noriter.article.domain.dto.study.StudyPostRequest;
import com.codewarts.noriter.article.service.StudyService;
import com.codewarts.noriter.auth.jwt.JwtProvider;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        Long memberId = jwtProvider.decode(request.getHeader("Authorization"));
        studyService.register(studyPostRequest, memberId);
    }

    @GetMapping
    public List<StudyListResponse> gatheringList(
        @RequestParam(required = false) Boolean completed) {
        return studyService.findList(completed);
    }

    @GetMapping("/{id}")
    public StudyDetailResponse gatheringDetail(@PathVariable Long id) {
        return studyService.findDetail(id);
    }

    @DeleteMapping("/{id}")
    public void studyRemove(@PathVariable Long id, HttpServletRequest request) {
        Long memberId = jwtProvider.decode(request.getHeader("Authorization"));
        studyService.delete(id, memberId);
    }

    @PatchMapping("/{id}")
    public void recruitmentCompletionUpdate(@PathVariable Long id, HttpServletRequest request) {
        Long memberId = jwtProvider.decode(request.getHeader("Authorization"));
        studyService.updateCompletion(id, memberId);
    }

    @PutMapping("/{id}")
    public void postEdit(@PathVariable Long id, @RequestBody StudyEditRequest studyEditRequest,HttpServletRequest request) {
        Long memberId = jwtProvider.decode(request.getHeader("Authorization"));
        studyService.update(id, studyEditRequest, memberId);
    }
}
