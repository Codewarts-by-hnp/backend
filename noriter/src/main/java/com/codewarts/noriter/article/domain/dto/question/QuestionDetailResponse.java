package com.codewarts.noriter.article.domain.dto.question;

import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.article.domain.Question;
import com.codewarts.noriter.comment.domain.dto.CommentResponse;
import com.codewarts.noriter.member.domain.dto.WriterInfoResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class QuestionDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final WriterInfoResponse writer;
    private final List<String> hashtag;
    private final LocalDateTime writtenTime;
    private final LocalDateTime editedTime;
    private final int wishCount;
    private final List<CommentResponse> comment;
    private final boolean completed;

    public QuestionDetailResponse(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.writer = WriterInfoResponse.from(question.getWriter());
        this.hashtag = question.getHashtags().stream()
            .map(Hashtag::getContent)
            .collect(Collectors.toList());
        this.writtenTime = question.getWrittenTime();
        this.editedTime = question.getEditedTime();
        this.wishCount = question.getWishList().size();
        this.comment = question.getComments().stream()
            .map(CommentResponse::new)
            .collect(Collectors.toList());
        this.completed = question.isCompleted();
    }

    public static QuestionDetailResponse from(Question question) {
        return new QuestionDetailResponse(question);
    }
}
