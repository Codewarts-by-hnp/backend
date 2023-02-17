package com.codewarts.noriter.article.dto.free;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.Hashtag;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FreeListResponse {

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
    private boolean wish;
    private int wishCount;
    private int commentCount;
    private boolean completed;

    public FreeListResponse(Article article, boolean sameWriter, boolean wish) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.writerNickname = article.getWriter().getNickname();
        this.sameWriter = sameWriter;
        this.hashtags = article.getHashtags().stream()
            .map(Hashtag::getContent)
            .collect(Collectors.toList());
        this.writtenTime = article.getWrittenTime();
        this.editedTime = article.getWrittenTime();
        this.wish = wish;
        this.wishCount = article.getWishList().size();
        this.commentCount = article.getComments().size();
        this.completed = isCompleted();
    }
}
