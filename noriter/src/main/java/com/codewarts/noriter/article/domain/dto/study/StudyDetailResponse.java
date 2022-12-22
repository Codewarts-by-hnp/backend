package com.codewarts.noriter.article.domain.dto.study;

import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.article.domain.Study;
import com.codewarts.noriter.comment.domain.dto.CommentResponse;
import com.codewarts.noriter.member.domain.dto.WriterInfoResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyDetailResponse {

    private Long id;
    private String title;
    private String content;
    private WriterInfoResponse writer;
    private List<String> hashtag;
    private LocalDateTime writtenTime;
    private LocalDateTime editedTime;
    private int wishCount;
    private boolean completed;
    private List<CommentResponse> comment;

    public StudyDetailResponse(Study study) {
        this.id = study.getId();
        this.title = study.getTitle();
        this.content = study.getContent();
        this.writer = new WriterInfoResponse(study.getWriter());
        this.hashtag = study.getHashtags().stream()
            .map(Hashtag::getContent)
            .collect(Collectors.toList());
        this.writtenTime = study.getWrittenTime();
        this.editedTime = study.getEditedTime();
        this.wishCount = study.getWishList().size();
        this.completed = study.isCompleted();
        this.comment = study.getComments().stream()
            .map(CommentResponse::new)
            .collect(Collectors.toList());
    }
}
