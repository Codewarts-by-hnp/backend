package com.codewarts.noriter.wish.domain;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.member.domain.Member;
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
public class Wish {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Article article;

  public Wish(Member member, Article article) {
    this.member = member;
    this.article = article;
  }
}
