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
public class Gathering extends Article {
    @Enumerated(EnumType.STRING)
    private StatusType status;

    public void changeStatusToComplete() {
        status = StatusType.COMPLETE;
    }

    public void changeStatusToIncomplete() {
        status = StatusType.INCOMPLETE;
    }
}
