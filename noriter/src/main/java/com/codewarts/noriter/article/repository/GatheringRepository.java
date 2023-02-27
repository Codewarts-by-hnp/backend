package com.codewarts.noriter.article.repository;

import com.codewarts.noriter.article.domain.Study;
import com.codewarts.noriter.article.domain.type.StatusType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GatheringRepository extends JpaRepository<Study, Long> {

    @Query("select s from Study s where s.status = :status")
    List<Study> findStudyByCompleted(@Param("status") StatusType status);

    @Query("select s from Study s")
    List<Study> findAllStudy();
}
