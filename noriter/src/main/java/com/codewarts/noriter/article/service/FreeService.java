package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.dto.free.FreeDetailResponse;
import com.codewarts.noriter.article.domain.dto.free.FreeEditRequest;
import com.codewarts.noriter.article.domain.dto.free.FreeListResponse;
import com.codewarts.noriter.article.domain.dto.free.FreePostRequest;
import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.member.service.MemberService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FreeService {

    private final ArticleRepository articleRepository;
    private final MemberService memberService;
    private final ArticleUtils articleUtils;

    @Transactional
    public void create(FreePostRequest freePostRequest, Long writerId) {
        Member member = memberService.findMember(writerId);
        Article free = freePostRequest.toEntity(member);
        free.addHashtags(freePostRequest.getHashtags());
        articleRepository.save(free);
    }

    public FreeDetailResponse findDetail(Long id, String accessToken) {
        Article article = findArticle(id);
        Long writerId = article.getWriter().getId();
        return new FreeDetailResponse(article, articleUtils.isSameWriter(writerId, accessToken));
    }

    public List<FreeListResponse> findList(String accessToken) {
        List<Article> freeTypeArticle = articleRepository.findAllByArticleType(ArticleType.FREE);
        return freeTypeArticle.stream()
            .map(article -> new FreeListResponse(article,
                articleUtils.isSameWriter(article.getWriter().getId(), accessToken)))
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(Long id, FreeEditRequest request, Long writerId) {
        memberService.findMember(writerId);
        Article free = findArticle(id);
        free.validateWriterOrThrow(writerId);
        free.update(request.getTitle(), request.getContent(), request.getHashtags());
    }

    @Transactional
    public void delete(Long id, Long writerId) {
        memberService.findMember(writerId);
        Article free = findArticle(id);
        free.validateWriterOrThrow(writerId);
        articleRepository.deleteByIdAndWriterId(id, writerId);
    }

    public Article findArticle(Long id) {
        return articleRepository.findById(id)
            .orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
    }
}
