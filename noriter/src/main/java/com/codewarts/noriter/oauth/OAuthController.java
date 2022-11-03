package com.codewarts.noriter.oauth;

import com.codewarts.noriter.domain.Member;
import com.codewarts.noriter.oauth.jwt.JwtProvider;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
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

    @GetMapping("/{resource-server}/loginform")
    public void redirectLoginForm(HttpServletResponse response,
        @PathVariable(name = "resource-server") String resourceServer) throws IOException {
        String loginFormUrl = mapper.getOAuthProperties(resourceServer).getLoginFormUrl();
        response.sendRedirect(loginFormUrl);
    }

    @PostMapping("/{resource-server}/login")
    public Map<String, String> login(@PathVariable(name = "resource-server") String resourceServer,
        @RequestBody Map<String, String> map, HttpServletResponse response) {

        OAuthService oAuthService = oauthServiceMap.get(resourceServer);
        OAuthAccessToken oAuthAccessToken = oAuthService.reqeustAccessToken(map.get("code"));

        Member oauthUser = oAuthService.reqeustUserInfo(oAuthAccessToken);
        Member loginMember = loginService.login(oauthUser);

        String jwtAccessToken = jwtProvider.issueAccessToken(loginMember.getId());
        String jwtRefreshToken = jwtProvider.issueRefreshToken(loginMember.getId());

        response.setHeader("Access-Token", jwtAccessToken);
        response.setHeader("Refresh-Token", jwtRefreshToken);
        return Collections.singletonMap("profileImageUrl", loginMember.getProfileImageUrl());
    }
}
