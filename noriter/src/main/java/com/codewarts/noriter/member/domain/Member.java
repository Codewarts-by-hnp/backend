package com.codewarts.noriter.member.domain;

import com.codewarts.noriter.article.domain.Article;
import com.codewarts.noriter.auth.oauth.type.ResourceServer;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "writer")
    private final List<Article> articles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ResourceServer resourceServer;
    private Long resourceServerId;
    private String nickname;
    private String email;
    private String profileImageUrl;
    private String refreshToken;

    @Builder
    public Member(ResourceServer resourceServer, Long resourceServerId,
        String nickname, String email, String profileImageUrl, String refreshToken) {
        this.resourceServer = resourceServer;
        this.resourceServerId = resourceServerId;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
