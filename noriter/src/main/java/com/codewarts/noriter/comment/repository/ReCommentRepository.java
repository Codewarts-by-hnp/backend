package com.codewarts.noriter.comment.repository;

import com.codewarts.noriter.comment.domain.ReComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReCommentRepository extends JpaRepository<ReComment, Long> {

}
