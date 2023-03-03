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
public class PlaygroundService extends ArticleService {

    private final ArticleRepository articleRepository;
    private final WishRepository wishRepository;

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
        List<Article> playgroundTypeArticle = articleRepository.findAllByArticleType(
            ArticleType.PLAYGROUND);
        if (memberId == null) {
            return playgroundTypeArticle.stream()
                .map(article -> new PlaygroundListResponse(article, false, false))
                .collect(Collectors.toList());
        }
        Member member = findMember(memberId);
        // Todo -> N+1 해결하기
        return playgroundTypeArticle.stream()
            .map(article -> new PlaygroundListResponse(article,
                article.getWriter().getId().equals(memberId),
                wishRepository.existsByArticleAndMember(article, member)))
            .collect(Collectors.toList());
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

    private Article findNotDeletedArticle(Long id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        if (article.isDeleted()) {
            throw new GlobalNoriterException(ArticleExceptionType.DELETED_ARTICLE);
        }
        return article;
    }

    Member findMember(Long id) {
        return super.findMember(id);
    }
}
