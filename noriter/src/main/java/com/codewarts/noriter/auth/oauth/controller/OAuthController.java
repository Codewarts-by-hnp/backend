package com.codewarts.noriter.auth.oauth.controller;

import static com.codewarts.noriter.auth.utils.OAuthUtils.ACCESS_TOKEN;
import static com.codewarts.noriter.auth.utils.OAuthUtils.REFRESH_TOKEN;

import com.codewarts.noriter.auth.jwt.JwtProvider;
import com.codewarts.noriter.auth.oauth.dto.OAuthAccessToken;
import com.codewarts.noriter.auth.oauth.properties.OAuthPropertiesMapper;
import com.codewarts.noriter.auth.oauth.service.OAuthService;
import com.codewarts.noriter.auth.service.LoginService;
import com.codewarts.noriter.common.domain.Member;
import com.codewarts.noriter.exception.GlobalNoriterException;
import com.codewarts.noriter.exception.type.AuthExceptionType;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class OAuthController {

    private final OAuthPropertiesMapper mapper;
    private final Map<String, OAuthService> oauthServiceMap;
    private final LoginService loginService;
    private final JwtProvider jwtProvider;

    @GetMapping("/{resource-server}/login/form")
    public void redirectLoginForm(HttpServletResponse response,
        @PathVariable(name = "resource-server") String resourceServer) throws IOException {
        if (!oauthServiceMap.containsKey(resourceServer)) {
            throw new GlobalNoriterException(AuthExceptionType.RESOURCE_SERVER_NOT_FOUND);
        }
        String loginFormUrl = mapper.getOAuthProperties(resourceServer).getLoginFormUrl();
        response.sendRedirect(loginFormUrl);
    }

    @PostMapping("/{resource-server}/login")
    public Map<String, String> login(@PathVariable(name = "resource-server") String resourceServer,
        @RequestBody Map<String, String> map, HttpServletResponse response) {

        OAuthService oAuthService = oauthServiceMap.get(resourceServer);

        String code = map.get("code");
        if (code == null) {
            throw new GlobalNoriterException(AuthExceptionType.EMPTY_AUTHORIZATION_CODE);
        }

        OAuthAccessToken oAuthAccessToken = oAuthService.requestAccessToken(code);

        Member oauthUser = oAuthService.reqeustUserInfo(oAuthAccessToken);
        Member loginMember = loginService.login(oauthUser);

        String jwtAccessToken = jwtProvider.issueAccessToken(loginMember.getId());
        String jwtRefreshToken = jwtProvider.issueRefreshToken(loginMember.getId());
        loginService.updateRefreshToken(jwtRefreshToken, loginMember.getId());

        response.setHeader(ACCESS_TOKEN, jwtAccessToken);
        response.setHeader(REFRESH_TOKEN, jwtRefreshToken);
        return Collections.singletonMap("profileImageUrl", loginMember.getProfileImageUrl());
    }

    @GetMapping("reissue/access-token")
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(REFRESH_TOKEN);
        Long decodedMemberId = jwtProvider.decode(refreshToken);
        String reissuedAccessToken = jwtProvider.issueAccessToken(decodedMemberId);

        response.setHeader(ACCESS_TOKEN, reissuedAccessToken);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_TOKEN);
        Long decodedMemberId = jwtProvider.decode(refreshToken);

        loginService.deleteRefreshToken(decodedMemberId);
    }
}
