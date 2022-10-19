package com.codewarts.noriter.domain;

import com.codewarts.noriter.domain.article.Article;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Article article;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Member writer;

  private boolean secret;
  private boolean deleted;
  private LocalDateTime writtenTime;
  private LocalDateTime editedTime;
}
