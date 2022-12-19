package com.codewarts.noriter.article.repository;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.type.ArticleType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a where a.articleType = :articleType")
    List<Article> findAllByArticleType(@Param("articleType") ArticleType articleType);
    Article findByIdAndWriterId(Long id, Long memberId);
    void deleteByIdAndWriterId(Long id, Long writerId);
}
