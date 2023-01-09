package com.codewarts.noriter.article.domain.dto.question;

import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.article.domain.type.StatusType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class QuestionListResponse {

    private final Long id;
    private final String title;
    private final String writerName;
    private final boolean sameWriter;
    private final List<String> hashtag;
    private final LocalDateTime writtenTime;
    private final LocalDateTime editedTime;
    private final int wishCount;
    private final int commentCount;
    private final StatusType status;

    public QuestionListResponse(Question question, boolean sameWriter) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.writerName = question.getWriter().getNickname();
        this.sameWriter = sameWriter;
        this.hashtag = question.getHashtags().stream().map(Hashtag::getContent).collect(Collectors.toList());
        this.writtenTime = question.getWrittenTime();
        this.editedTime = question.getEditedTime();
        this.wishCount = question.getWishList().size();
        this.commentCount = question.getComments().size();
        this.status = question.getStatus();
    }
}
