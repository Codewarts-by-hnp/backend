package com.codewarts.noriter.common.config;


import com.codewarts.noriter.auth.jwt.JwtProperties;
import com.codewarts.noriter.auth.oauth.properties.OAuthPropertiesMapper;
import com.codewarts.noriter.common.converter.StatusTypeConverter;
import com.codewarts.noriter.auth.AuthVerificationArgumentResolver;
import io.netty.resolver.DefaultAddressResolverGroup;
import java.util.List;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.netty.http.client.HttpClient;


@Configuration
@EnableWebMvc
@EnableConfigurationProperties({JwtProperties.class, OAuthPropertiesMapper.class})
public class WebConfig implements WebMvcConfigurer {

    private final AuthVerificationArgumentResolver authVerificationArgumentResolver;

    public WebConfig(AuthVerificationArgumentResolver authVerificationArgumentResolver) {
        this.authVerificationArgumentResolver = authVerificationArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
       resolvers.add(authVerificationArgumentResolver);
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
            .resolver(DefaultAddressResolverGroup.INSTANCE);
    }

    @Bean
    public WebClient webClient(HttpClient httpClient) {
        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StatusTypeConverter.StringToStatusTypeConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .exposedHeaders("*")
            .allowedHeaders("*")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }
}
