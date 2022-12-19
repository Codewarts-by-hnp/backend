package com.codewarts.noriter.article.repository;

import com.codewarts.noriter.article.domain.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select q from Question q where q.id =:id")
    Optional<Question> findByQuestionId(@Param("id") Long id);

    @Query("select q from Question q")
    List<Question> findAllQuestion();

    @Query("select q from Question q where q.completed = :status")
    List<Question> findQuestionByCompleted(@Param("status") boolean status);

    @Query("select q from Question q where q.id = :id")
    Optional<Question> findQuestionById(@Param("id") Long id);

    @Query("select q from Question q where q.id =:id and q.writer.id = :writerId")
    Optional<Question> findByQuestionIdAndWriterId(@Param("id") Long id, @Param("writerId") Long writerId);
}
