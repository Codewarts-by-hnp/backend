package com.codewarts.noriter.article.domain.dto.free;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.article.domain.Hashtag;
import com.codewarts.noriter.comment.domain.dto.CommentResponse;
import com.codewarts.noriter.member.domain.dto.WriterInfoResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FreeDetailResponse {

    private Long id;
    private String title;
    private String content;
    private WriterInfoResponse writer;
    private List<String> hashtag;
    private LocalDateTime writtenTime;
    private LocalDateTime editedTime;
    private int wishCount;
    private List<CommentResponse> comment;

    public FreeDetailResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.writer = new WriterInfoResponse(article.getWriter());
        this.hashtag = article.getHashtags().stream()
            .map(Hashtag::getContent)
            .collect(Collectors.toList());
        this.writtenTime = article.getWrittenTime();
        this.editedTime = article.getEditedTime();
        this.wishCount = article.getWishList().size();
        this.comment = article.getComments().stream()
            .map(CommentResponse::new)
            .collect(Collectors.toList());
    }
}
