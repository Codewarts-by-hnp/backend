package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.article.dto.article.ArticleCreateRequest;
import com.codewarts.noriter.article.dto.article.ArticleListResponse;
import com.codewarts.noriter.article.dto.article.ArticleUpdateRequest;
import com.codewarts.noriter.article.dto.playground.PlaygroundDetailResponse;
import com.codewarts.noriter.article.dto.playground.PlaygroundListResponse;
import com.codewarts.noriter.article.repository.ArticleRepository;
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
public class PlaygroundService extends ArticleService {

    private final ArticleRepository articleRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;


    @Transactional
    @Override
    public Long create(ArticleCreateRequest playgroundCreateRequest, Long writerId) {
        Member member = findMember(writerId);
        Article playground = playgroundCreateRequest.toEntity(member);
        return articleRepository.save(playground).getId();
    }

    @Override
    public PlaygroundDetailResponse findDetail(Long id, Long memberId) {
        Article article = findNotDeletedArticle(id);
        Long writerId = article.getWriter().getId();
        boolean sameWriter = writerId.equals(memberId);
        if (memberId == null) {
            return new PlaygroundDetailResponse(article, sameWriter, false);
        }
        Member member = findMember(writerId);
        boolean wish = wishRepository.existsByArticleAndMember(article, member);
        return new PlaygroundDetailResponse(article, sameWriter, wish);
    }

    @Override
    public List<ArticleListResponse> findList(Long memberId) {
        return articleRepository.findAllByArticleType(ArticleType.PLAYGROUND).stream()
            .map(article -> new PlaygroundListResponse(article,
                isSameWriter(article, memberId),
                isWished(article, memberId)
            )).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void update(Long id, ArticleUpdateRequest request, Long writerId) {
        findMember(writerId);
        Article playground = findNotDeletedArticle(id);
        playground.validateWriterOrThrow(writerId);
        playground.update(request.getTitle(), request.getContent(), request.getHashtags());
    }

    @Override
    @Transactional
    public void delete(Long id, Long writerId) {
        findMember(writerId);
        Article playground = findNotDeletedArticle(id);
        playground.validateWriterOrThrow(writerId);
        playground.delete();
    }

    @Override
    Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new GlobalNoriterException(
            MemberExceptionType.MEMBER_NOT_FOUND));
    }

    private Article findNotDeletedArticle(Long id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        if (article.isDeleted()) {
            throw new GlobalNoriterException(ArticleExceptionType.DELETED_ARTICLE);
        }
        return article;
    }

    private boolean isSameWriter(Article article, Long memberId) {
        if (memberId == null) return false;
        return article.getWriter().getId().equals(memberId);
    }

    private boolean isWished(Article article, Long memberId) {
        if (memberId == null) return false;
        Member member = findMember(memberId);
        return wishRepository.existsByArticleAndMember(article, member);
    }
}
