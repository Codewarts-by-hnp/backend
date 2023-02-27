package com.codewarts.noriter.article.repository;

import com.codewarts.noriter.article.domain.Gathering;
import com.codewarts.noriter.article.domain.type.StatusType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {

    @Query("select s from Gathering s where s.status = :status")
    List<Gathering> findByGatheringCompleted(@Param("status") StatusType status);

    @Query("select s from Gathering s")
    List<Gathering> findAllGathering();
}
