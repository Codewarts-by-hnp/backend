package com.codewarts.noriter.auth.oauth.controller;

import static com.codewarts.noriter.auth.utils.OAuthUtils.ACCESS_TOKEN;
import static com.codewarts.noriter.auth.utils.OAuthUtils.REFRESH_TOKEN;

import com.codewarts.noriter.auth.oauth.type.ResourceServer;
import com.codewarts.noriter.auth.service.LoginService;
import com.codewarts.noriter.member.domain.Member;
import com.codewarts.noriter.member.repository.MemberRepository;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MockOAuthController {
    private final MemberRepository memberRepository;
    private final LoginService loginService;
    private Long resourceServerId = 0L;

    @PostMapping("/mock/github/login")
    public Map<String, String> login(HttpServletResponse response) {

        Member mockMember = requestUserInfo();
        Member mockLoginMember = memberRepository.save(mockMember);

        String accessToken = String.valueOf(mockLoginMember.getId());
        String refreshToken = String.valueOf(mockLoginMember.getId());

        loginService.updateRefreshToken(refreshToken, mockLoginMember.getId());

        response.setHeader(ACCESS_TOKEN, accessToken);
        response.setHeader(REFRESH_TOKEN, refreshToken);

        return Collections.singletonMap("profileImageUrl", mockLoginMember.getProfileImageUrl());
    }

    @PostMapping("mock/logout")
    public void logout(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_TOKEN);
        long mockMemberId = Long.parseLong(refreshToken);

        loginService.deleteRefreshToken(mockMemberId);
    }

    private Member requestUserInfo() {
        String mockEmail = UUID.randomUUID().toString();
        return new Member(ResourceServer.GITHUB, resourceServerId++, "mockNickname", mockEmail,
            "mockProfileImageUrl", "mockRefreshToken");
    }

}
