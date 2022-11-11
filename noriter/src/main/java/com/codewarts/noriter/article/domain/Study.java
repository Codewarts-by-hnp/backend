package com.codewarts.noriter.article.domain;

import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.common.domain.Member;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends Article {

    private boolean completed;

    @Builder
    public Study(String title, String content, Member writer) {
        super(title, content, writer, ArticleType.STUDY);
        this.completed = false;
    }
}
