package com.codewarts.noriter.article.repository;

import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.type.StatusType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long>, CustomArticleRepository {

    @Query("select q from Question q")
    List<Question> findAllQuestion();

    @Query("select q from Question q where q.status = :status")
    List<Question> findQuestionByCompleted(@Param("status") StatusType status);
}
