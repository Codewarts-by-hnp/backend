package com.codewarts.noriter.article.dto.gathering;

import com.codewarts.noriter.article.domain.Gathering;
import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GatheringListResponse {

    private Long id;
    private String title;
    private String content;
    private String writerNickname;
    private boolean sameWriter;
    private List<String> hashtags;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedTime;
    private boolean wish;
    private int wishCount;
    private int commentCount;
    private StatusType status;

    public GatheringListResponse(Gathering gathering, boolean sameWriter, boolean wish) {
        this.id = gathering.getId();
        this.title = gathering.getTitle();
        this.content = gathering.getContent();
        this.writerNickname = gathering.getWriter().getNickname();
        this.sameWriter = sameWriter;
        this.hashtags = gathering.getHashtags().stream().map(Hashtag::getContent)
            .collect(Collectors.toList());
        this.createdTime = gathering.getCreatedTime();
        this.lastModifiedTime = gathering.getLastModifiedTime();
        this.wish = wish;
        this.wishCount = gathering.getWishList().size();
        this.commentCount = gathering.getComments().size();
        this.status = gathering.getStatus();
    }
}
