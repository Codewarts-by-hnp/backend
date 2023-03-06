package com.codewarts.noriter.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.codewarts.noriter.auth.jwt.JwtProvider;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.AuthExceptionType;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthVerificationArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginCheck.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

//        if (parseMethod(webRequest).equals(HttpMethod.OPTIONS.name())) {
//            return null;
//        }

        String accessToken = parseAuthorizationHeader(webRequest);
        String method = parseHttpMethod(webRequest);
        boolean hasAccessToken = StringUtils.hasText(accessToken);

        if (!hasAccessToken && method.equals(HttpMethod.GET.name())) {
            return null;
        }
        if (!hasAccessToken) {
            throw new GlobalNoriterException(AuthExceptionType.EMPTY_ACCESS_TOKEN);
        }
        try {
            jwtProvider.verifyToken(accessToken);
        } catch (TokenExpiredException expiredException) {
            throw new GlobalNoriterException(AuthExceptionType.EXPIRED_ACCESS_TOKEN);
        } catch (JWTVerificationException verificationException) {
            throw new GlobalNoriterException(AuthExceptionType.TAMPERED_ACCESS_TOKEN);
        }
        return jwtProvider.decode(accessToken);
    }

    private String parseAuthorizationHeader(NativeWebRequest webRequest) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        return Objects.requireNonNull(request).getHeader("Authorization");
    }

    private String parseHttpMethod(NativeWebRequest webRequest) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        return Objects.requireNonNull(request).getMethod();
    }
}
