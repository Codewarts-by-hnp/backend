package com.codewarts.noriter.oauth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubAccessTokenRequest {

    private final String clientId;
    private final String clientSecret;
    private final String code;

    @Builder
    public GithubAccessTokenRequest(String clientId, String clientSecret, String code) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
    }

    public static GithubAccessTokenRequest of(String code, OAuthProperties OAuthProperties) {
        return GithubAccessTokenRequest.builder()
            .clientId(OAuthProperties.getClientId())
            .clientSecret(OAuthProperties.getClientSecret())
            .code(code)
            .build();
    }
}
