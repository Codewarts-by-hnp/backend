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
public class Study extends Article {

    private boolean completed;

    public Study(String title, String content, Member writer, LocalDateTime writtenTime,
        LocalDateTime editedTime, ArticleType articleType, boolean completed) {
        super(title, content, writer, writtenTime, editedTime, articleType);
        this.completed = completed;
    }

    public void completion() {
        completed = true;
    }

    public void incomplete() {
        completed = false;
    }

    @Override
    public void update(String title, String content, List<String> requestHashtags) {
        super.update(title, content, requestHashtags);
    }

    @Override
    public void checkWriter(Long writerId) {
        super.checkWriter(writerId);
    }
}
