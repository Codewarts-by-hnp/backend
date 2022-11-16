package com.codewarts.noriter.article.domain.dto.study;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.article.domain.Study;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyListResponse {

    private Long id;
    private String title;
    private String content;
    private List<String> hashtag;
    private LocalDateTime writtenTime;
    private LocalDateTime editedTime;
    private int wishCount;
    private int commentCount;
    private boolean completed;

    public StudyListResponse(Study study) {
        this.id = study.getId();
        this.title = study.getTitle();
        this.content = study.getContent();
        this.hashtag = study.getHashtags().stream().map(Hashtag::getContent).collect(Collectors.toList());
        this.writtenTime = study.getWrittenTime();
        this.editedTime = study.getEditedTime();
        this.wishCount = study.getWishList().size();
        this.commentCount = study.getComments().size();
        this.completed = study.isCompleted();
    }

    public StudyListResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.hashtag = article.getHashtags().stream().map(Hashtag::getContent).collect(Collectors.toList());
        this.writtenTime = article.getWrittenTime();
        this.editedTime = article.getEditedTime();
        this.wishCount = article.getWishList().size();
        this.commentCount = article.getComments().size();
        this.completed = isCompleted();
    }
}
