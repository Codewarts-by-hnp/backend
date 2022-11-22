package com.codewarts.noriter.article.domain;

import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.common.domain.Member;
import java.util.List;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Article {

    private boolean completed;

    @Builder
    public Question(String title, String content, Member writer) {
        super(title, content, writer, ArticleType.QUESTION);
        this.completed = false;
    }

    public void changeStatus(boolean status) {
        completed = status;
    }

    @Override
    public void update(String newTitle, String newContent, List<String> newHashTags) {
        super.update(newTitle, newContent, newHashTags);
    }
    public void changeCompletedTrue() {
        this.completed = true;
    }

    public void changeCompletedFalse() {
        this.completed = false;
    }
}
