package com.codewarts.noriter.domain.article;

import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends Article {

  private boolean completed;

}
