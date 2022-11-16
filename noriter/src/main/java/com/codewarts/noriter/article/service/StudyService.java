package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.Study;
import com.codewarts.noriter.article.domain.dto.study.StudyListResponse;
import com.codewarts.noriter.article.domain.dto.study.StudyPostRequest;
import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.common.domain.Member;
import com.codewarts.noriter.common.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public void register(StudyPostRequest studyPostRequest, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(IllegalArgumentException::new);
        Study study = studyPostRequest.toEntity(member);
        study.addHashtags(studyPostRequest.getHashtags());
        articleRepository.save(study);
    }

    public List<StudyListResponse> findStudyList(Boolean completed) {

        if (completed == null) {
            List<Article> allByArticleType = articleRepository.findAllByArticleType(
                ArticleType.STUDY);
            return allByArticleType.stream()
                .map(StudyListResponse::new)
                .collect(Collectors.toList());
        }

        List<Study> allStudy = articleRepository.findStudyByCompleted(completed);
        return allStudy.stream()
            .map(StudyListResponse::new)
            .collect(Collectors.toList());
    }
}
