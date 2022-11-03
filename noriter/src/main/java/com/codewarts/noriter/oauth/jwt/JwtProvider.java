package com.codewarts.noriter.oauth.jwt;

import static javax.management.timer.Timer.ONE_HOUR;
import static javax.management.timer.Timer.ONE_WEEK;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final String issuer;
    private final Algorithm algorithm;

    public JwtProvider(JwtProperties jwtProperties) {
        this.issuer = jwtProperties.getIssuer();
        this.algorithm = Algorithm.HMAC256(jwtProperties.getSecretKey());
    }

    public String issueAccessToken(Long memberId) {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject("Access-Token")
            .withAudience(memberId.toString())
            .withIssuedAt(Date.from(Instant.now()))
            .withExpiresAt(Date.from(Instant.now().plusMillis(ONE_HOUR)))

            .withClaim("memberId", memberId)

            .sign(algorithm);
    }

    public String issueRefreshToken(Long memberId) {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject("Refresh-Token")
            .withAudience(memberId.toString())
            .withIssuedAt(Date.from(Instant.now()))
            .withExpiresAt(Date.from(Instant.now().plusMillis(ONE_WEEK)))

            .withClaim("memberId", memberId)

            .sign(algorithm);
    }
}
