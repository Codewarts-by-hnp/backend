package com.codewarts.noriter.oauth;

import com.codewarts.noriter.domain.Member;

public interface OAuthService {

    OAuthAccessToken reqeustAccessToken(String code);

    Member reqeustUserInfo(OAuthAccessToken oauthAccessToken);
}
