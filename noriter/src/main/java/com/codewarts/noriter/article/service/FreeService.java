package com.codewarts.noriter.article.service;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.dto.free.FreeDetailResponse;
import com.codewarts.noriter.article.dto.free.FreeEditRequest;
import com.codewarts.noriter.article.dto.free.FreeListResponse;
import com.codewarts.noriter.article.dto.free.FreePostRequest;
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
public class FreeService {

    private final ArticleRepository articleRepository;
    private final WishRepository wishRepository;
    private final MemberService memberService;

    @Transactional
    public Long create(FreePostRequest freePostRequest, Long writerId) {
        Member member = memberService.findMember(writerId);
        Article free = freePostRequest.toEntity(member);
        free.addHashtags(freePostRequest.getHashtags());
        return articleRepository.save(free).getId();
    }

    public FreeDetailResponse findDetail(Long id, Long memberId) {
        Article article = findArticle(id);
        Long writerId = article.getWriter().getId();
        boolean sameWriter = writerId.equals(memberId);
        if (memberId == null) {
            return new FreeDetailResponse(article, sameWriter, false);
        }
        Member member = memberService.findMember(memberId);
        boolean wish = wishRepository.existsByArticleAndMember(article, member);
        return new FreeDetailResponse(article, sameWriter, wish);
    }

    public List<FreeListResponse> findList(Long memberId) {
        List<Article> freeTypeArticle = articleRepository.findAllByArticleType(ArticleType.FREE);
        if (memberId == null) {
            return freeTypeArticle.stream()
                .map(article -> new FreeListResponse(article, false, false))
                    .collect(Collectors.toList());
        }
        Member member = memberService.findMember(memberId);
        // Todo -> N+1 해결하기
        return freeTypeArticle.stream()
            .map(article -> new FreeListResponse(article,
                article.getWriter().getId().equals(memberId),
                wishRepository.existsByArticleAndMember(article, member)))
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
