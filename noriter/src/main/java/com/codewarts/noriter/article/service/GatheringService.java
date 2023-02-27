package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Study;
import com.codewarts.noriter.article.dto.gathering.GatheringDetailResponse;
import com.codewarts.noriter.article.dto.gathering.GatheringUpdateRequest;
import com.codewarts.noriter.article.dto.gathering.GatheringListResponse;
import com.codewarts.noriter.article.dto.gathering.GatheringCreateRequest;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.article.repository.GatheringRepository;
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
public class GatheringService {

    private final MemberService memberService;
    private final ArticleRepository articleRepository;
    private final GatheringRepository gatheringRepository;
    private final WishRepository wishRepository;

    @Transactional
    public Long create(GatheringCreateRequest gatheringCreateRequest, Long memberId) {
        Member member = memberService.findMember(memberId);
        Study study = gatheringCreateRequest.toEntity(member);
        study.addHashtags(gatheringCreateRequest.getHashtags());
        return gatheringRepository.save(study).getId();
    }

    public List<GatheringListResponse> findList(StatusType status, Long memberId) {
        if (status == null && memberId == null) {
            return gatheringRepository.findAllStudy().stream()
                .map(study -> new GatheringListResponse(study, false, false))
                .collect(Collectors.toList());
        }
        if (memberId == null) {
            return gatheringRepository.findStudyByCompleted(status).stream()
                .map(study -> new GatheringListResponse(study, false, false))
                .collect(Collectors.toList());
        }
        Member member = memberService.findMember(memberId);
        if (status == null) {
            return gatheringRepository.findAllStudy().stream()
                .map(study -> new GatheringListResponse(study,
                    study.getWriter().getId().equals(memberId),
                    wishRepository.existsByArticleAndMember(study, member)))
                .collect(Collectors.toList());
        }
        return gatheringRepository.findStudyByCompleted(status).stream()
            .map(study -> new GatheringListResponse(study, study.getWriter().getId().equals(memberId),
                wishRepository.existsByArticleAndMember(study, member)))
            .collect(Collectors.toList());
    }

    public GatheringDetailResponse findDetail(Long id, Long memberId) {
        Study study = findNotDeletedStudy(id);
        boolean sameWriter = study.getWriter().getId().equals(memberId);

        if (memberId == null) {
            return new GatheringDetailResponse(study, false, false);
        }
        Member member = memberService.findMember(memberId);
        boolean wish = wishRepository.existsByArticleAndMember(study, member);

        return new GatheringDetailResponse(study, sameWriter, wish);
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
    public void update(Long id, GatheringUpdateRequest request, Long writerId) {
        memberService.findMember(writerId);
        Study study = findNotDeletedStudy(id);
        study.validateWriterOrThrow(writerId);
        study.update(request.getTitle(), request.getContent(), request.getHashtags());
    }

    public Study findNotDeletedStudy(Long id) {
        Study study = gatheringRepository.findById(id).
            orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        if (study.isDeleted()) {
            throw new GlobalNoriterException(ArticleExceptionType.DELETED_ARTICLE);
        }
        return study;
    }
}
