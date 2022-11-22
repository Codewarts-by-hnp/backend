package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.Study;
import com.codewarts.noriter.article.domain.dto.study.StudyDetailResponse;
import com.codewarts.noriter.article.domain.dto.study.StudyEditRequest;
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

    public List<StudyListResponse> findList(Boolean completed) {

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

    public StudyDetailResponse findDetail(Long id) {
        Study study = articleRepository.findByStudyId(id).orElseThrow(RuntimeException::new);
        return new StudyDetailResponse(study);
    }

    @Transactional
    public void delete(Long id, Long writerId) {
        articleRepository.deleteByIdAndWriterId(id, writerId);
    }

    @Transactional
    public void updateCompletion(Long id, Long writerId, boolean completion) {
        Study study = articleRepository.findByStudyIdAndWriterId(id, writerId)
            .orElseThrow(RuntimeException::new);

        if (completion) {study.completion();
        } else {
            study.incomplete();
        }
    }

    @Transactional
    public void update(Long id, StudyEditRequest request, Long writerId) {
        Study study = articleRepository.findByStudyIdAndWriterId(id, writerId)
            .orElseThrow(RuntimeException::new);
        study.update(request.getTitle(), request.getContent(), request.getHashtags());
    }
}
