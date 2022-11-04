package com.codewarts.noriter.auth.oauth.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuthProperties {

    private final String callbackUrl;
    private final String clientId;
    private final String clientSecret;
    private final String accessTokenApiUrl;
    private final String loginFormUrl;
    private final String userInfoApiUrl;
}
