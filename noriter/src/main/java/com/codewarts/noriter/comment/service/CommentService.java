package com.codewarts.noriter.comment.service;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.comment.domain.Comment;
import com.codewarts.noriter.comment.domain.ReComment;
import com.codewarts.noriter.comment.dto.recomment.ReCommentRequest;
import com.codewarts.noriter.comment.repository.CommentRepository;
import com.codewarts.noriter.comment.repository.ReCommentRepository;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.CommentExceptionType;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ReCommentRepository reCommentRepository;
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberService memberService;

    public void createReComment(Long articleId, Long commentId, Long memberId,
        ReCommentRequest request) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        if (article.isDeleted()) {
            throw new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND);
        }
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new GlobalNoriterException(CommentExceptionType.COMMENT_NOT_FOUND));
        if (comment.isDeleted()) {
            throw new GlobalNoriterException(CommentExceptionType.COMMENT_NOT_FOUND);
        }
        Member member = memberService.findMember(memberId);

        ReComment reComment = request.toEntity(comment, member);
        reCommentRepository.save(reComment);
    }
}
