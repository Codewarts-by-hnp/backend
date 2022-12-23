package com.codewarts.noriter.auth.oauth.dto;

import static com.codewarts.noriter.auth.oauth.type.ResourceServer.GITHUB;

import com.codewarts.noriter.member.domain.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubUserInfo {

    private Long id;
    private String login;
    private String email;
    private String avatarUrl;

    public Member toMember() {
        return Member.builder()
            .resourceServerId(id)
            .resourceServer(GITHUB)
            .nickname(login)
            .email(email)
            .profileImageUrl(avatarUrl)
            .build();
    }
}

