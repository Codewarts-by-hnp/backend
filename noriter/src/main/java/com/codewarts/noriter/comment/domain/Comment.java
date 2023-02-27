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

  @OneToMany(mappedBy = "comment")
  private List<ReComment> reComments = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Member writer;

  private String content;
  private boolean secret;
  private boolean deleted;

  @Builder
  public Comment(Long id, Article article, List<ReComment> reComments, Member writer,
      String content, boolean secret, boolean deleted) {
    this.id = id;
    this.article = article;
    this.reComments = reComments;
    this.writer = writer;
    this.content = content;
    this.secret = secret;
    this.deleted = deleted;
  }

  public void update(String content, boolean secret) {
    this.content = content;
    this.secret = secret;
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

  public void delete() {
    this.deleted = true;
  }
}
