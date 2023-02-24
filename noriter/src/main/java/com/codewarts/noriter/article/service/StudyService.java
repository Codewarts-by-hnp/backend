package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Study;
import com.codewarts.noriter.article.dto.study.StudyDetailResponse;
import com.codewarts.noriter.article.dto.study.StudyEditRequest;
import com.codewarts.noriter.article.dto.study.StudyListResponse;
import com.codewarts.noriter.article.dto.study.StudyPostRequest;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.article.repository.StudyRepository;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.member.service.MemberService;
import com.codewarts.noriter.wish.repository.WishRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyService {

    private final MemberService memberService;
    private final ArticleRepository articleRepository;
    private final StudyRepository studyRepository;
    private final WishRepository wishRepository;

    @Transactional
    public Long create(StudyPostRequest studyPostRequest, Long memberId) {
        Member member = memberService.findMember(memberId);
        Study study = studyPostRequest.toEntity(member);
        study.addHashtags(studyPostRequest.getHashtags());
        return studyRepository.save(study).getId();
    }

    public List<StudyListResponse> findList(StatusType status, Long memberId) {
        if (status == null && memberId == null) {
            return studyRepository.findAllStudy().stream()
                .map(study -> new StudyListResponse(study, false, false))
                .collect(Collectors.toList());
        }
        if (memberId == null) {
            return studyRepository.findStudyByCompleted(status).stream()
                .map(study -> new StudyListResponse(study, false, false))
                .collect(Collectors.toList());
        }
        Member member = memberService.findMember(memberId);
        if (status == null) {
            return studyRepository.findAllStudy().stream()
                .map(study -> new StudyListResponse(study,
                    study.getWriter().getId().equals(memberId),
                    wishRepository.existsByArticleAndMember(study, member)))
                .collect(Collectors.toList());
        }
        return studyRepository.findStudyByCompleted(status).stream()
            .map(study -> new StudyListResponse(study, study.getWriter().getId().equals(memberId),
                wishRepository.existsByArticleAndMember(study, member)))
            .collect(Collectors.toList());
    }

    public StudyDetailResponse findDetail(Long id, Long memberId) {
        Study study = findNotDeletedStudy(id);
        boolean sameWriter = study.getWriter().getId().equals(memberId);

        if (memberId == null) {
            return new StudyDetailResponse(study, false, false);
        }
        Member member = memberService.findMember(memberId);
        boolean wish = wishRepository.existsByArticleAndMember(study, member);

        return new StudyDetailResponse(study, sameWriter, wish);
    }

    @Transactional
    public void delete(Long id, Long writerId) {
        memberService.findMember(writerId);
        Study study = findNotDeletedStudy(id);
        study.validateWriterOrThrow(writerId);
        study.delete();
    }

    @Transactional
    public void updateCompletion(Long id, Long writerId, StatusType status) {
        memberService.findMember(writerId);
        Study study = findNotDeletedStudy(id);
        study.validateWriterOrThrow(writerId);

        if (study.getStatus() == status) {
            throw new GlobalNoriterException(ArticleExceptionType.ALREADY_CHANGED_STATUS);
        }
        if (status == StatusType.COMPLETE) {
            study.completion();
        } else {
            study.incomplete();
        }
    }

    @Transactional
    public void update(Long id, StudyEditRequest request, Long writerId) {
        memberService.findMember(writerId);
        Study study = findNotDeletedStudy(id);
        study.validateWriterOrThrow(writerId);
        study.update(request.getTitle(), request.getContent(), request.getHashtags());
    }

    public Study findNotDeletedStudy(Long id) {
        Study study = studyRepository.findById(id).
            orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        if (study.isDeleted()) {
            throw new GlobalNoriterException(ArticleExceptionType.DELETED_ARTICLE);
        }
        return study;
    }
}
