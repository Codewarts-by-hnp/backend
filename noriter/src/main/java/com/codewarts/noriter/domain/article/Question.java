package com.codewarts.noriter.domain.article;

import com.codewarts.noriter.domain.article.Article;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Article {

  private boolean solved;

}
