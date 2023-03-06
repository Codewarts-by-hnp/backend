package com.codewarts.noriter.article.dto.article;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.type.StatusType;
import com.codewarts.noriter.comment.dto.comment.CommentResponse;
import com.codewarts.noriter.member.dto.WriterInfoResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class ArticleDetailResponse {

    private Long id;
    private String title;
    private String content;
    private WriterInfoResponse writer;
    private boolean sameWriter;
    private List<String> hashtags;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedTime;
    private boolean wish;
    private int wishCount;
    private StatusType status;
    private List<CommentResponse> comment;

    public ArticleDetailResponse(Article article, boolean sameWriter, boolean wish) {}
}
