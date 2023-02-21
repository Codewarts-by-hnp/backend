package com.codewarts.noriter.comment.repository;

import com.codewarts.noriter.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
