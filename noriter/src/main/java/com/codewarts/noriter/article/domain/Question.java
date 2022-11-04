package com.codewarts.noriter.article.domain;

import com.codewarts.noriter.article.domain.Article;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Article {

  private boolean solved;

}
