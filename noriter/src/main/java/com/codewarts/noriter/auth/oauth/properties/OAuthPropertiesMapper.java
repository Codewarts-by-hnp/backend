package com.codewarts.noriter.auth.oauth.properties;

import com.codewarts.noriter.auth.oauth.properties.OAuthProperties;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "auth")
public class OAuthPropertiesMapper {

    private final Map<String, OAuthProperties> vendors;

    public OAuthProperties getOAuthProperties(String vendor) {
        return vendors.get(vendor);
    }
}
