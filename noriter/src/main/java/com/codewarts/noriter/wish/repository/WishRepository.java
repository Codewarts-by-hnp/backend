package com.codewarts.noriter.wish.repository;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.wish.domain.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {

    boolean existsWishByArticleAndAndMember(Article article, Member member);
}
