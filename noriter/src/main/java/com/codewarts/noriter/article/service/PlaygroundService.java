package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.dto.playground.PlaygroundDetailResponse;
import com.codewarts.noriter.article.dto.playground.PlaygroundUpdateRequest;
import com.codewarts.noriter.article.dto.playground.PlaygroundListResponse;
import com.codewarts.noriter.article.dto.playground.PlaygroundCreateRequest;
import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.article.repository.ArticleRepository;
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
public class PlaygroundService {

    private final ArticleRepository articleRepository;
    private final WishRepository wishRepository;
    private final MemberService memberService;

    @Transactional
    public Long create(PlaygroundCreateRequest playgroundCreateRequest, Long writerId) {
        Member member = memberService.findMember(writerId);
        Article playground = playgroundCreateRequest.toEntity(member);
        return articleRepository.save(playground).getId();
    }

    public PlaygroundDetailResponse findDetail(Long id, Long memberId) {
        Article article = findNotDeletedArticle(id);
        Long writerId = article.getWriter().getId();
        boolean sameWriter = writerId.equals(memberId);
        if (memberId == null) {
            return new PlaygroundDetailResponse(article, sameWriter, false);
        }
        Member member = memberService.findMember(memberId);
        boolean wish = wishRepository.existsByArticleAndMember(article, member);
        return new PlaygroundDetailResponse(article, sameWriter, wish);
    }

    public List<PlaygroundListResponse> findList(Long memberId) {
        List<Article> playgroundTypeArticle = articleRepository.findAllByArticleType(
            ArticleType.PLAYGROUND);
        if (memberId == null) {
            return playgroundTypeArticle.stream()
                .map(article -> new PlaygroundListResponse(article, false, false))
                .collect(Collectors.toList());
        }
        Member member = memberService.findMember(memberId);
        // Todo -> N+1 해결하기
        return playgroundTypeArticle.stream()
            .map(article -> new PlaygroundListResponse(article,
                article.getWriter().getId().equals(memberId),
                wishRepository.existsByArticleAndMember(article, member)))
            .collect(Collectors.toList());
    }

    @Transactional
    public void update(Long id, PlaygroundUpdateRequest request, Long writerId) {
        memberService.findMember(writerId);
        Article playground = findNotDeletedArticle(id);
        playground.validateWriterOrThrow(writerId);
        playground.update(request.getTitle(), request.getContent(), request.getHashtags());
    }

    @Transactional
    public void delete(Long id, Long writerId) {
        memberService.findMember(writerId);
        Article playground = findNotDeletedArticle(id);
        playground.validateWriterOrThrow(writerId);
        playground.delete();
    }

    public Article findNotDeletedArticle(Long id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        if (article.isDeleted()) {
            throw new GlobalNoriterException(ArticleExceptionType.DELETED_ARTICLE);
        }
        return article;
    }
}
