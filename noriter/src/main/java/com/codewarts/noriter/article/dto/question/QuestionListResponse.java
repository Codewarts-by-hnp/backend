package com.codewarts.noriter.article.dto.question;

import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class QuestionListResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String writerNickname;
    private final boolean sameWriter;
    private final List<String> hashtags;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime writtenTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime editedTime;
    private final boolean wish;
    private final int wishCount;
    private final int commentCount;
    private final StatusType status;

    public QuestionListResponse(Question question, boolean sameWriter, boolean wish) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.writerNickname = question.getWriter().getNickname();
        this.sameWriter = sameWriter;
        this.hashtags = question.getHashtags().stream().map(Hashtag::getContent).collect(Collectors.toList());
        this.writtenTime = question.getWrittenTime();
        this.editedTime = question.getEditedTime();
        this.wish = wish;
        this.wishCount = question.getWishList().size();
        this.commentCount = question.getComments().size();
        this.status = question.getStatus();
    }
}
