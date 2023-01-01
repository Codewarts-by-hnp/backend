package com.codewarts.noriter.article.domain;

import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.member.domain.Member;
import java.time.LocalDateTime;
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
public class Question extends Article {

    @Enumerated(EnumType.STRING)
    private StatusType status;

    public Question(String title, String content, Member writer, LocalDateTime writtenTime,
        LocalDateTime editedTime, ArticleType articleType, StatusType status) {
        super(title, content, writer, writtenTime, editedTime, articleType);
        this.status = status;
    }

    public void changeStatusToComplete() {
        this.status = StatusType.COMPLETE;
    }

    public void changeStatusToIncomplete() {
        this.status = StatusType.INCOMPLETE;
    }
}
