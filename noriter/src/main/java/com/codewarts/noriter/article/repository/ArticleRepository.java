package com.codewarts.noriter.article.repository;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.Study;
import com.codewarts.noriter.article.domain.type.ArticleType;
import java.util.List;
import com.codewarts.noriter.article.domain.Question;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a where a.articleType = :articleType")
    List<Article> findAllByArticleType(@Param("articleType") ArticleType articleType);

    @Query("select s from Study s where s.completed = :completed")
    List<Study> findStudyByCompleted(@Param("completed") boolean completed);

    @Query("select s from Study s where s.id =:id")
    Optional<Study> findByStudyId(@Param("id") Long id);

    @Query("select s from Study s where s.id =:id and s.writer.id =:writerId")
    Optional<Study> findByStudyIdAndWriterId(@Param("id") Long id, @Param("writerId") Long writerId);

    @Query("select q from Question q where q.id =:id")
    Optional<Question> findByQuestionId(@Param("id") Long id);

    @Query("select q from Question q")
    List<Question> findAllQuestion();

    @Query("select q from Question q where q.completed = :status")
    List<Question> findQuestionByCompleted(@Param("status") boolean status);

    @Query("select q from Question q where q.id = :id")
    Optional<Question> findQuestionById(@Param("id") Long id);

    void deleteByIdAndWriterId(Long id, Long writerId);
    @Query("select q from Question q where q.id =:id and q.writer.id = :writerId")
    Optional<Question> findByQuestionIdAndWriterId(@Param("id") Long id, @Param("writerId") Long writerId);
}
