package com.codewarts.noriter.wish.service;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.WishExceptionType;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.member.service.MemberService;
import com.codewarts.noriter.wish.domain.Wish;
import com.codewarts.noriter.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ArticleRepository articleRepository;
    private final MemberService memberService;

    public void create(Long memberId, Long articleId) {
        Member member = memberService.findMember(memberId);
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new GlobalNoriterException(
                ArticleExceptionType.ARTICLE_NOT_FOUND));

        boolean existsWish = wishRepository.existsWishByArticleAndAndMember(article, member);
        if (existsWish) {
            throw new GlobalNoriterException(WishExceptionType.WISH_ALREADY_EXIST);
        }
        wishRepository.save(new Wish(member, article));
    }
}
