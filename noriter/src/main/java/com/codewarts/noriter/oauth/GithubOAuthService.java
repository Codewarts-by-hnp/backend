package com.codewarts.noriter.oauth;

import com.codewarts.noriter.domain.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("github")
public class GithubOAuthService implements OAuthService {
    private final WebClient webClient;
    private final OAuthProperties oauthProperties;

    public GithubOAuthService(WebClient webClient, OAuthPropertiesMapper oauthPropertiesMapper) {
        this.webClient = webClient;
        this.oauthProperties = oauthPropertiesMapper.getOAuthProperties("github");
    }

    @Override
    public OAuthAccessToken reqeustAccessToken(String code) {
        GithubAccessTokenResponse githubAccessTokenResponse = webClient
            .post()
            .uri(oauthProperties.getAccessTokenApiUrl())
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(
                GithubAccessTokenRequest.of(
                    code,
                    oauthProperties
                )
            )
            .retrieve()
            .bodyToMono(GithubAccessTokenResponse.class)
            .block();

        return githubAccessTokenResponse.toOAuthAccessToken();
    }

    @Override
    public Member reqeustUserInfo(OAuthAccessToken oauthAccessToken) {
        GithubUserInfo githubUserInfo = webClient
            .get()
            .uri(oauthProperties.getUserInfoApiUrl())
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, oauthAccessToken.getAuthorizationValue())
            .retrieve()
            .bodyToMono(GithubUserInfo.class)
            .block();

        return githubUserInfo.toMember();
    }
}
