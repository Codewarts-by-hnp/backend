package com.codewarts.noriter.article.repository;

import com.codewarts.noriter.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
