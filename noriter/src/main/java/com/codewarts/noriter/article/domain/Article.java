package com.codewarts.noriter.article.domain;

import com.codewarts.noriter.article.domain.type.ArticleType;
import com.codewarts.noriter.comment.domain.Comment;
import com.codewarts.noriter.common.domain.Member;
import com.codewarts.noriter.wish.domain.Wish;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member writer;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Hashtag> hashtags = new ArrayList<>();

    @OneToMany(mappedBy = "article", orphanRemoval = true)
    private final List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "article", orphanRemoval = true)
    private final List<Wish> wishList = new ArrayList<>();

    @OneToMany(mappedBy = "article", orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    private LocalDateTime writtenTime;
    private LocalDateTime editedTime;

    @Enumerated(EnumType.STRING)
    private ArticleType articleType;
    private boolean deleted;

    public Article(String title, String content, Member writer, ArticleType articleType) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.writtenTime = LocalDateTime.now();
        this.editedTime = LocalDateTime.now();
        this.articleType = articleType;
        this.deleted = false;
    }

    public void addHashtags(List<String> requestHashtags) {
        if (ObjectUtils.isEmpty(requestHashtags)) {
            return;
        }
        for (String requestHashtag : requestHashtags) {
            this.hashtags.add(Hashtag.builder().article(this).content(requestHashtag).build());
        }
    }

    protected void update(String newTitle, String newContent , List<String> requestHashtags) {
        this.title = newTitle;
        this.content = newContent;
        this.editedTime = LocalDateTime.now();
        this.hashtags.clear();
        addHashtags(requestHashtags);
    }
}
