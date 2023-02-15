package com.codewarts.noriter.wish.repository;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.wish.domain.Wish;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {

    boolean existsByArticleAndMember(Article article, Member member);
    Optional<Wish> findByArticleAndMember(Article article, Member member);
}
