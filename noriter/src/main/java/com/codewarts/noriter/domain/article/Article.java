package com.codewarts.noriter.domain.article;

import com.codewarts.noriter.domain.Comment;
import com.codewarts.noriter.domain.Hashtag;
import com.codewarts.noriter.domain.Image;
import com.codewarts.noriter.domain.Member;
import com.codewarts.noriter.domain.Wish;
import com.codewarts.noriter.domain.type.ArticleType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Member writer;

  @OneToMany(mappedBy = "article")
  private List<Hashtag> hashtags = new ArrayList<>();

  @OneToMany(mappedBy = "article")
  private List<Image> images = new ArrayList<>();

  @OneToMany(mappedBy = "article")
  private List<Wish> wishList = new ArrayList<>();

  @OneToMany(mappedBy = "article")
  private List<Comment> comments = new ArrayList<>();

  private LocalDateTime writtenTime;
  private LocalDateTime editedTime;

  @Enumerated(EnumType.STRING)
  private ArticleType articleType;
  private boolean deleted;

}
