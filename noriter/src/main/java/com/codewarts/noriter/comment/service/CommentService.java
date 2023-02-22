package com.codewarts.noriter.comment.service;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.comment.domain.Comment;
import com.codewarts.noriter.comment.domain.ReComment;
import com.codewarts.noriter.comment.dto.comment.CommentPostRequest;
import com.codewarts.noriter.comment.dto.comment.CommentUpdateRequest;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReCommentRepository reCommentRepository;
    private final ArticleRepository articleRepository;
    private final MemberService memberService;

    public void createComment(Long memberId, Long articleId, CommentPostRequest request) {
        Member member = memberService.findMember(memberId);
        Article article = findNotDeletedArticle(articleId);
        Comment comment = request.toEntity(article, member);
        commentRepository.save(comment);
    }

    public void updateComment(Long memberId, Long articleId, Long commentId, CommentUpdateRequest request) {
        Article article = findNotDeletedArticle(articleId);
        Member member = memberService.findMember(memberId);
        Comment comment = findNotDeletedComment(commentId);
        comment.validateOrThrow(member, article);
        comment.update(request.getContent(), request.getSecret());
    }

    public void createReComment(Long articleId, Long commentId, Long memberId,
        ReCommentRequest request) {
        findNotDeletedArticle(articleId);
        Comment comment = findNotDeletedComment(commentId);
        Member member = memberService.findMember(memberId);
        ReComment reComment = request.toEntity(comment, member);
        reCommentRepository.save(reComment);
    }

    public Article findNotDeletedArticle(Long id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        if (article.isDeleted()) {
            throw new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND);
        }
        return article;
    }

    public Comment findNotDeletedComment(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new GlobalNoriterException(CommentExceptionType.COMMENT_NOT_FOUND));
        if (comment.isDeleted()) {
            throw new GlobalNoriterException(CommentExceptionType.DELETED_COMMENT);
        }
        return comment;
    }
}
