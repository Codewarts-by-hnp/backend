package com.codewarts.noriter.comment.domain;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class Comment {

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
  private LocalDateTime writtenTime;
  private LocalDateTime editedTime;

  @Builder
  public Comment(Long id, Article article, List<ReComment> reComments, Member writer,
      String content, boolean secret, boolean deleted, LocalDateTime writtenTime, LocalDateTime editedTime) {
    this.id = id;
    this.article = article;
    this.reComments = reComments;
    this.writer = writer;
    this.content = content;
    this.secret = secret;
    this.deleted = deleted;
    this.writtenTime = writtenTime;
    this.editedTime = editedTime;
  }
}
