package com.codewarts.noriter.article.repository;

import com.codewarts.noriter.article.domain.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatheringRepository extends JpaRepository<Gathering, Long>, CustomArticleRepository {

}
