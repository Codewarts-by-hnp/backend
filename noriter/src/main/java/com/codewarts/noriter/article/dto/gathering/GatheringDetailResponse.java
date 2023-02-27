package com.codewarts.noriter.article.dto.gathering;

import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.article.domain.Study;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.comment.dto.comment.CommentResponse;
import com.codewarts.noriter.member.dto.WriterInfoResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GatheringDetailResponse {

    private Long id;
    private String title;
    private String content;
    private WriterInfoResponse writer;
    private boolean sameWriter;
    private List<String> hashtags;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime writtenTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime editedTime;
    private boolean wish;
    private int wishCount;
    private StatusType status;
    private List<CommentResponse> comment;

    public GatheringDetailResponse(Study study, boolean sameWriter, boolean wish) {
        this.id = study.getId();
        this.title = study.getTitle();
        this.content = study.getContent();
        this.writer = new WriterInfoResponse(study.getWriter());
        this.sameWriter = sameWriter;
        this.hashtags = study.getHashtags().stream()
            .map(Hashtag::getContent)
            .collect(Collectors.toList());
        this.writtenTime = study.getWrittenTime();
        this.editedTime = study.getEditedTime();
        this.wish = wish;
        this.wishCount = study.getWishList().size();
        this.status = study.getStatus();
        this.comment = study.getComments().stream()
            .map(CommentResponse::new)
            .collect(Collectors.toList());
    }
}
