package com.codewarts.noriter.oauth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubAccessTokenResponse {

    private String accessToken;
    private String tokenType;

    public OAuthAccessToken toOAuthAccessToken() {
        return OAuthAccessToken.builder()
            .tokenValue(accessToken)
            .tokenType(tokenType)
            .build();
    }
}
