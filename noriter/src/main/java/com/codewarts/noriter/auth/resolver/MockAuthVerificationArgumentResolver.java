package com.codewarts.noriter.auth.resolver;

import com.codewarts.noriter.auth.LoginCheck;
import com.codewarts.noriter.auth.jwt.JwtProvider;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.AuthExceptionType;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
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
@Profile("performance")
public class MockAuthVerificationArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginCheck.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String accessToken = parseAuthorizationHeader(webRequest);
        String method = parseHttpMethod(webRequest);
        boolean hasAccessToken = StringUtils.hasText(accessToken);

        if (!hasAccessToken && method.equals(HttpMethod.GET.name())) {
            return null;
        }
        if (!hasAccessToken) {
            throw new GlobalNoriterException(AuthExceptionType.EMPTY_ACCESS_TOKEN);
        }
        return Long.parseLong(accessToken);
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
