package com.codewarts.noriter.article.domain;

import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.common.domain.Member;
import java.util.List;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends Article {

    private boolean completed;

    @Builder
    public Study(String title, String content, Member writer) {
        super(title, content, writer, ArticleType.STUDY);
        this.completed = false;
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
}
