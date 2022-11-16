package com.codewarts.noriter.article.domain.dto.question;

import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.article.domain.Question;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class QuestionResponse {

    Long id;
    String title;
    String writerName;
    List<String> hashtag;
    LocalDateTime writtenTime;
    LocalDateTime editedTime;
    int wishCount;
    int commentCount;
    boolean completed;

    public QuestionResponse(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.writerName = question.getWriter().getNickname();
        this.hashtag = question.getHashtags().stream().map(Hashtag::getContent).collect(Collectors.toList());
        this.writtenTime = question.getWrittenTime();
        this.editedTime = question.getEditedTime();
        this.wishCount = question.getWishList().size();
        this.commentCount = question.getComments().size();
        this.completed = question.isCompleted();
    }
}
