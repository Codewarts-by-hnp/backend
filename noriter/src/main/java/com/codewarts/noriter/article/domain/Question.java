package com.codewarts.noriter.article.domain;

import com.codewarts.noriter.article.domain.type.StatusType;
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

    public void changeStatusToComplete() {
        this.status = StatusType.COMPLETE;
    }

    public void changeStatusToIncomplete() {
        this.status = StatusType.INCOMPLETE;
    }
}
