package com.codewarts.noriter.oauth;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OAuthController {

    private final OAuthPropertiesMapper mapper;

    @GetMapping("/{resource-server}/loginform")
    public void redirectLoginForm(HttpServletResponse response,
        @PathVariable(name = "resource-server") String resourceServer) throws IOException {
        String loginFormUrl = mapper.getOAuthProperties(resourceServer).getLoginFormUrl();
        response.sendRedirect(loginFormUrl);
    }
}
