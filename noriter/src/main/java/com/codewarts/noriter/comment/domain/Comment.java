package com.codewarts.noriter.comment.domain;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.common.util.BaseTimeEntity;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.CommentExceptionType;
import com.codewarts.noriter.member.domain.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Article article;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Comment parent;

  @OneToMany(mappedBy = "parent")
  private List<Comment> children = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Member writer;

  private String content;
  private boolean secret;
  private boolean deleted;

  @Builder
  public Comment(Long id, Article article, Comment parent,
      List<Comment> children, Member writer, String content, boolean secret, boolean deleted) {
    this.id = id;
    this.article = article;
    this.parent = parent;
    this.children = children;
    this.writer = writer;
    this.content = content;
    this.secret = secret;
    this.deleted = deleted;
  }

  public void update(String content, boolean secret) {
    this.content = content;
    this.secret = secret;
  }
  public void delete() {
    this.deleted = true;
    if (this.parent != null) {
      this.parent.removeChild(this);
    }
  }

  public void addChild(Comment comment) {
    this.children.add(comment);
  }

  public void removeChild(Comment comment) {
    this.children.remove(comment);
  }

  public void validateChildOrThrow() {
    if (this.parent != null ) {
      throw new GlobalNoriterException(CommentExceptionType.NOT_ALLOWED_RECOMMENT);
    }
  }

  public void validateWriterOrThrow(Member writer) {
    if (!Objects.equals(this.writer, writer)) {
      throw new GlobalNoriterException(CommentExceptionType.NOT_MATCHED_WRITER);
    }
  }

  public void validateArticleOrThrow(Article article) {
    if (!Objects.equals(this.article, article)) {
      throw new GlobalNoriterException(CommentExceptionType.NOT_MATCHED_ARTICLE);
    }
  }
}
