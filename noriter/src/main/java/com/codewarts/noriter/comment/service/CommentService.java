package com.codewarts.noriter.comment.service;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.comment.domain.Comment;
import com.codewarts.noriter.comment.domain.ReComment;
import com.codewarts.noriter.comment.dto.comment.CommentCreateRequest;
import com.codewarts.noriter.comment.dto.comment.CommentUpdateRequest;
import com.codewarts.noriter.comment.dto.recomment.ReCommentCreateRequest;
import com.codewarts.noriter.comment.dto.recomment.ReCommentUpdateRequest;
import com.codewarts.noriter.comment.repository.CommentRepository;
import com.codewarts.noriter.comment.repository.ReCommentRepository;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.CommentExceptionType;
import com.codewarts.noriter.exception.type.MemberExceptionType;
import com.codewarts.noriter.exception.type.ReCommentExceptionType;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReCommentRepository reCommentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public void createComment(Long memberId, Long articleId, CommentCreateRequest request) {
        Member member = findMember(memberId);
        Article article = findNotDeletedArticle(articleId);
        Comment comment = request.toEntity(article, member);
        commentRepository.save(comment);
    }

    public void updateComment(Long memberId, Long articleId, Long commentId, CommentUpdateRequest request) {
        Member member = findMember(memberId);
        Article article = findNotDeletedArticle(articleId);
        Comment comment = findNotDeletedComment(commentId);
        comment.validateArticleOrThrow(article);
        comment.validateWriterOrThrow(member);
        comment.update(request.getContent(), request.getSecret());
    }

    public void deleteComment(Long memberId, Long articleId, Long commentId) {
        Member member = findMember(memberId);
        Article article = findNotDeletedArticle(articleId);
        Comment comment = findNotDeletedComment(commentId);
        comment.validateArticleOrThrow(article);
        comment.validateWriterOrThrow(member);
        comment.delete();
    }

    public void createReComment(Long articleId, Long commentId, Long memberId,
        ReCommentCreateRequest request) {
        Member member = findMember(memberId);
        Article article = findNotDeletedArticle(articleId);

        Comment comment = findNotDeletedComment(commentId);
        comment.validateArticleOrThrow(article);
        ReComment reComment = request.toEntity(comment, member);

        reCommentRepository.save(reComment);
    }

    public void updateReComment(Long articleId, Long commentId, Long recommentId, Long memberId,
        ReCommentUpdateRequest request) {
        Member member = findMember(memberId);
        Article article = findNotDeletedArticle(articleId);

        Comment comment = findNotDeletedComment(commentId);
        comment.validateArticleOrThrow(article);

        ReComment reComment = findNotDeletedReComment(recommentId);
        reComment.validateCommentOrThrow(comment);
        reComment.validateWriterOrThrow(member);
        reComment.update(request.getContent(), request.getSecret());
    }

    private Article findNotDeletedArticle(Long id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        if (article.isDeleted()) {
            throw new GlobalNoriterException(ArticleExceptionType.DELETED_ARTICLE);
        }
        return article;
    }

    private Comment findNotDeletedComment(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new GlobalNoriterException(CommentExceptionType.COMMENT_NOT_FOUND));
        if (comment.isDeleted()) {
            throw new GlobalNoriterException(CommentExceptionType.DELETED_COMMENT);
        }
        return comment;
    }

    private ReComment findNotDeletedReComment(Long id) {
        ReComment reComment = reCommentRepository.findById(id)
            .orElseThrow(
                () -> new GlobalNoriterException(ReCommentExceptionType.RECOMMENT_NOT_FOUND));
        if (reComment.isDeleted()) {
            throw new GlobalNoriterException(ReCommentExceptionType.DELETED_RECOMMENT);
        }
        return reComment;
    }

    public void deleteRecomment(Long articleId, Long commentId, Long recommentId, Long memberId) {
        Member member = findMember(memberId);
        Article article = findNotDeletedArticle(articleId);

        Comment comment = findNotDeletedComment(commentId);
        comment.validateArticleOrThrow(article);

        ReComment reComment = findNotDeletedReComment(recommentId);
        reComment.validateCommentOrThrow(comment);
        reComment.validateWriterOrThrow(member);
        reComment.delete();
    }
    private Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new GlobalNoriterException(
            MemberExceptionType.MEMBER_NOT_FOUND));
    }
}
