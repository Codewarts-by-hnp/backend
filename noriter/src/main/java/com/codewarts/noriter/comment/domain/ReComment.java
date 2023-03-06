package com.codewarts.noriter.comment.domain;

import com.codewarts.noriter.common.util.BaseTimeEntity;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.ReCommentExceptionType;
import com.codewarts.noriter.member.domain.Member;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member writer;

    private String content;
    private boolean secret;
    private boolean deleted;

    public ReComment(Comment comment, Member writer, String content, boolean secret) {
        this.comment = comment;
        this.writer = writer;
        this.content = content;
        this.secret = secret;
        this.deleted = false;
    }

    public void update(String content, Boolean secret) {
        this.content = content;
        this.secret = secret;
    }

    public void delete() {
        this.deleted = true;
    }

    public void validateWriterOrThrow(Member writer) {
        if (!Objects.equals(this.writer, writer)) {
            throw new GlobalNoriterException(ReCommentExceptionType.RECOMMENT_NOT_MATCHED_WRITER);
        }
    }

    public void validateCommentOrThrow(Comment comment) {
        if (!Objects.equals(this.comment, comment)) {
            throw new GlobalNoriterException(ReCommentExceptionType.NOT_MATCHED_COMMENT);
        }
    }
}
