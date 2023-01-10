package com.codewarts.noriter.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.codewarts.noriter.auth.jwt.JwtProvider;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.AuthExceptionType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class AuthVerificationInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {

        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        String method = request.getMethod();
        if (method.equals(HttpMethod.GET.name())) {
            return true;
        }

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(accessToken)) {
            throw new GlobalNoriterException(AuthExceptionType.EMPTY_ACCESS_TOKEN);
        }
        try {
            jwtProvider.verifyToken(accessToken);
        } catch (TokenExpiredException expiredException) {
            throw new GlobalNoriterException(AuthExceptionType.EXPIRED_ACCESS_TOKEN);
        } catch (JWTVerificationException verificationException) {
            throw new GlobalNoriterException(AuthExceptionType.TAMPERED_ACCESS_TOKEN);
        }
        Long memberId = jwtProvider.decode(accessToken);
        request.setAttribute("memberId", memberId);
        return true;
    }
}
