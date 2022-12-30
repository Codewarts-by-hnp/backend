package com.codewarts.noriter.article.domain;

import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.member.domain.Member;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends Article {

    @Enumerated(EnumType.STRING)
    private StatusType status;

    public Study(String title, String content, Member writer, LocalDateTime writtenTime,
        LocalDateTime editedTime, ArticleType articleType, StatusType status) {
        super(title, content, writer, writtenTime, editedTime, articleType);
        this.status = status;
    }

    public void completion() {
        status = StatusType.COMPLETE;
    }

    public void incomplete() {
        status = StatusType.INCOMPLETE;
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
