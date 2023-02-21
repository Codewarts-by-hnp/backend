package com.codewarts.noriter.comment.domain;

import com.codewarts.noriter.member.domain.Member;
import java.time.LocalDateTime;
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
public class ReComment {

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
    private LocalDateTime writtenTime;
    private LocalDateTime editedTime;

    public ReComment(Comment comment, Member writer, String content, boolean secret) {
        this.comment = comment;
        this.writer = writer;
        this.content = content;
        this.secret = secret;
        this.deleted = false;
        this.writtenTime = LocalDateTime.now();
        this.editedTime = LocalDateTime.now();
    }
}
