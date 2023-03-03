package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Gathering;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.article.dto.article.ArticleCreateRequest;
import com.codewarts.noriter.article.dto.article.ArticleListResponse;
import com.codewarts.noriter.article.dto.article.ArticleUpdateRequest;
import com.codewarts.noriter.article.dto.gathering.GatheringDetailResponse;
import com.codewarts.noriter.article.dto.gathering.GatheringListResponse;
import com.codewarts.noriter.article.repository.GatheringRepository;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.MemberExceptionType;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.member.repository.MemberRepository;
import com.codewarts.noriter.wish.repository.WishRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GatheringService extends ArticleService {

    private final GatheringRepository gatheringRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;


    @Override
    @Transactional
    public Long create(ArticleCreateRequest gatheringCreateRequest, Long memberId) {
        Member member = findMember(memberId);
        Gathering gathering = (Gathering) gatheringCreateRequest.toEntity(member);
        return gatheringRepository.save(gathering).getId();
    }

    @Override
    public List<ArticleListResponse> findList(StatusType status, Long memberId) {
        return gatheringRepository.findAllGatheringList(status).stream()
            .map(gathering -> new GatheringListResponse(gathering,
                isSameWriter(gathering, memberId),
                isWished(gathering, memberId)))
            .collect(Collectors.toList());
    }

    @Override
    public GatheringDetailResponse findDetail(Long id, Long memberId) {
        Gathering gathering = findNotDeletedStudy(id);
        boolean sameWriter = gathering.getWriter().getId().equals(memberId);

        if (memberId == null) {
            return new GatheringDetailResponse(gathering, false, false);
        }
        Member member = findMember(memberId);
        boolean wish = wishRepository.existsByArticleAndMember(gathering, member);

        return new GatheringDetailResponse(gathering, sameWriter, wish);
    }

    @Override
    @Transactional
    public void delete(Long id, Long writerId) {
        findMember(writerId);
        Gathering gathering = findNotDeletedStudy(id);
        gathering.validateWriterOrThrow(writerId);
        gathering.delete();
    }

    @Transactional
    public void updateCompletion(Long id, Long writerId, StatusType status) {
        findMember(writerId);
        Gathering gathering = findNotDeletedStudy(id);
        gathering.validateWriterOrThrow(writerId);

        if (gathering.getStatus() == status) {
            throw new GlobalNoriterException(ArticleExceptionType.ALREADY_CHANGED_STATUS);
        }
        if (status == StatusType.COMPLETE) {
            gathering.changeStatusToComplete();
        } else {
            gathering.changeStatusToIncomplete();
        }
    }

    @Override
    @Transactional
    public void update(Long id, ArticleUpdateRequest request, Long writerId) {
        findMember(writerId);
        Gathering gathering = findNotDeletedStudy(id);
        gathering.validateWriterOrThrow(writerId);
        gathering.update(request.getTitle(), request.getContent(), request.getHashtags());
    }

    @Override
    Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new GlobalNoriterException(
            MemberExceptionType.MEMBER_NOT_FOUND));
    }

    private Gathering findNotDeletedStudy(Long id) {
        Gathering gathering = gatheringRepository.findById(id).
            orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        if (gathering.isDeleted()) {
            throw new GlobalNoriterException(ArticleExceptionType.DELETED_ARTICLE);
        }
        return gathering;
    }

    private boolean isSameWriter(Gathering gathering, Long memberId) {
        if (memberId == null) return false;
        return gathering.getWriter().getId().equals(memberId);
    }

    private boolean isWished(Gathering gathering, Long memberId) {
        if (memberId == null) return false;
        Member member = findMember(memberId);
        return wishRepository.existsByArticleAndMember(gathering, member);
    }
}
