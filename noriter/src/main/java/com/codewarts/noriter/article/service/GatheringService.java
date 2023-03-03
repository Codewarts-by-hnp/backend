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
import com.codewarts.noriter.member.domain.Member;
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

    @Override
    @Transactional
    public Long create(ArticleCreateRequest gatheringCreateRequest, Long memberId) {
        Member member = findMember(memberId);
        Gathering gathering = (Gathering) gatheringCreateRequest.toEntity(member);
        return gatheringRepository.save(gathering).getId();
    }

    @Override
    public List<ArticleListResponse> findList(StatusType status, Long memberId) {
        if (status == null && memberId == null) {
            return gatheringRepository.findAllGathering().stream()
                .map(gathering -> new GatheringListResponse(gathering, false, false))
                .collect(Collectors.toList());
        }
        if (memberId == null) {
            return gatheringRepository.findByGatheringCompleted(status).stream()
                .map(gathering -> new GatheringListResponse(gathering, false, false))
                .collect(Collectors.toList());
        }
        Member member = findMember(memberId);
        if (status == null) {
            return gatheringRepository.findAllGathering().stream()
                .map(gathering -> new GatheringListResponse(gathering,
                    gathering.getWriter().getId().equals(memberId),
                    wishRepository.existsByArticleAndMember(gathering, member)))
                .collect(Collectors.toList());
        }
        return gatheringRepository.findByGatheringCompleted(status).stream()
            .map(gathering -> new GatheringListResponse(gathering, gathering.getWriter().getId().equals(memberId),
                wishRepository.existsByArticleAndMember(gathering, member)))
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

    private Gathering findNotDeletedStudy(Long id) {
        Gathering gathering = gatheringRepository.findById(id).
            orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        if (gathering.isDeleted()) {
            throw new GlobalNoriterException(ArticleExceptionType.DELETED_ARTICLE);
        }
        return gathering;
    }

    Member findMember(Long id) {
        return super.findMember(id);
    }
}
