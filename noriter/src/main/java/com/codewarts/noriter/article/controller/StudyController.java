package com.codewarts.noriter.article.controller;

import com.codewarts.noriter.article.domain.dto.study.StudyDetailResponse;
import com.codewarts.noriter.article.domain.dto.study.StudyEditRequest;
import com.codewarts.noriter.article.domain.dto.study.StudyListResponse;
import com.codewarts.noriter.article.domain.dto.study.StudyPostRequest;
import com.codewarts.noriter.article.service.StudyService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/gathering")
@Validated
public class StudyController {

    private final StudyService studyService;

    @PostMapping
    public void register(@RequestBody @Valid StudyPostRequest studyPostRequest,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
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
        Long memberId = getMemberId(request);
        studyService.delete(id, memberId);
    }

    @PatchMapping("/{id}")
    public void recruitmentCompletionUpdate(@PathVariable Long id,
        @RequestBody Map<String, Boolean> map, HttpServletRequest request) {
        Long memberId = getMemberId(request);
        studyService.updateCompletion(id, memberId, map.get("completion"));
    }

    @PutMapping("/{id}")
    public void postEdit(@PathVariable Long id, @RequestBody StudyEditRequest studyEditRequest,
        HttpServletRequest request) {
        Long memberId = getMemberId(request);
        studyService.update(id, studyEditRequest, memberId);
    }

    private Long getMemberId(HttpServletRequest request) {
        return (Long) request.getAttribute("memberId");
    }
}
