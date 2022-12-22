package com.codewarts.noriter.article.domain;

import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.member.domain.Member;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Article {

    private boolean completed;

    public Question(String title, String content, Member writer, LocalDateTime writtenTime,
        LocalDateTime editedTime, ArticleType articleType, boolean completed) {
        super(title, content, writer, writtenTime, editedTime, articleType);
        this.completed = completed;
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
