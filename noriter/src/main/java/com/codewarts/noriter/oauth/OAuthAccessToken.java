package com.codewarts.noriter.oauth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAccessToken {

    private String tokenValue;
    private String tokenType;

    @Builder
    public OAuthAccessToken(String tokenValue, String tokenType) {
        this.tokenValue = tokenValue;
        this.tokenType = tokenType;
    }

    public String getAuthorizationValue() {
        return tokenType + " " + tokenValue;
    }
}
