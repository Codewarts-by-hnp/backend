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
        return issueToken("Access-Token", memberId, Date.from(Instant.now().plusMillis(ONE_HOUR)));
    }

    public String issueRefreshToken(Long memberId) {
        return issueToken("Refresh-Token", memberId, Date.from(Instant.now().plusMillis(ONE_WEEK)));
    }

    public Long decode(String token) {
        return JWT.decode(token)
            .getClaim("memberId")
            .asLong();
    }

    private String issueToken(String subject, Long memberId, Date expiresAt) {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(subject)
            .withAudience(memberId.toString())
            .withIssuedAt(Date.from(Instant.now()))
            .withExpiresAt(expiresAt)

            .withClaim("memberId", memberId)

            .sign(algorithm);
    }
}
