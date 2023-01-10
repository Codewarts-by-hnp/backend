package com.codewarts.noriter.article.domain.dto.study;

import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.article.domain.Study;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String writerNickname;
    private boolean sameWriter;
    private List<String> hashtags;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime writtenTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime editedTime;
    private int wishCount;
    private int commentCount;
    private StatusType status;

    public StudyListResponse(Study study, boolean sameWriter) {
        this.id = study.getId();
        this.title = study.getTitle();
        this.content = study.getContent();
        this.writerNickname = study.getWriter().getNickname();
        this.sameWriter = sameWriter;
        this.hashtags = study.getHashtags().stream().map(Hashtag::getContent)
            .collect(Collectors.toList());
        this.writtenTime = study.getWrittenTime();
        this.editedTime = study.getEditedTime();
        this.wishCount = study.getWishList().size();
        this.commentCount = study.getComments().size();
        this.status = study.getStatus();
    }
}
