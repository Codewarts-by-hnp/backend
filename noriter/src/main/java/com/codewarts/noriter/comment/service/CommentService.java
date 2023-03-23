package com.codewarts.noriter.comment.service;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.repository.ArticleRepository;
import com.codewarts.noriter.comment.domain.Comment;
import com.codewarts.noriter.comment.dto.comment.CommentCreateRequest;
import com.codewarts.noriter.comment.dto.comment.CommentUpdateRequest;
import com.codewarts.noriter.comment.repository.CommentRepository;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ArticleExceptionType;
import com.codewarts.noriter.exception.type.CommentExceptionType;
import com.codewarts.noriter.exception.type.MemberExceptionType;
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
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public void createComment(Long memberId, Long articleId, CommentCreateRequest request) {
        Member member = findMember(memberId);
        Article article = findNotDeletedArticle(articleId);
        Comment comment = request.toComment(article, member);
        commentRepository.save(comment);
    }

    public void updateComment(Long memberId, Long articleId, Long id, CommentUpdateRequest request) {
        Member member = findMember(memberId);
        Article article = findNotDeletedArticle(articleId);
        Comment comment = findNotDeletedComment(id);
        comment.validateArticleOrThrow(article);
        comment.validateWriterOrThrow(member);
        comment.update(request.getContent(), request.getSecret());
    }

    public void deleteComment(Long memberId, Long articleId, Long id) {
        Member member = findMember(memberId);
        Article article = findNotDeletedArticle(articleId);
        Comment comment = findNotDeletedComment(id);
        comment.validateArticleOrThrow(article);
        comment.validateWriterOrThrow(member);
        comment.delete();
    }

    public void createReComment(Long id, Long articleId, Long memberId,
        CommentCreateRequest request) {
        Member member = findMember(memberId);
        Article article = findNotDeletedArticle(articleId);
        Comment parentComment = findNotDeletedComment(id);
        parentComment.validateChildOrThrow();

        Comment childComment = request.toRecomment(article, member, parentComment);
        commentRepository.save(childComment);
        parentComment.addChild(childComment);
    }

    private Article findNotDeletedArticle(Long id) {
        Article article = articleRepository.findById(id)
            .orElseThrow(() -> new GlobalNoriterException(ArticleExceptionType.ARTICLE_NOT_FOUND));
        if (article.getDeleted() != null) {
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

    private Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new GlobalNoriterException(
            MemberExceptionType.MEMBER_NOT_FOUND));
    }
}
