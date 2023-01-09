package com.codewarts.noriter.article.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.codewarts.noriter.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Component
public class ArticleUtils {

    private final JwtProvider jwtProvider;

    protected boolean isSameWriter(Long writerId, String accessToken) {
        if (!StringUtils.hasText(accessToken)) {
            return false;
        }

        try {
            jwtProvider.verifyToken(accessToken);
            return jwtProvider.decode(accessToken).equals(writerId);
        } catch (JWTVerificationException ignored) {
            return false;
        }
    }
}
