package com.codewarts.noriter.article.repository;

import com.codewarts.noriter.article.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>, CustomArticleRepository {

}
