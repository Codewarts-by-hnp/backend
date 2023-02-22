package com.codewarts.noriter.common.config;


import com.codewarts.noriter.auth.jwt.JwtProvider;
import com.codewarts.noriter.common.converter.StatusTypeConverter;
import com.codewarts.noriter.interceptor.AuthVerificationInterceptor;
import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;

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
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthVerificationInterceptor(jwtProvider)).order(1)
            .addPathPatterns("/community/playground", "/community/playground/{id}",
                "/community/gathering", "/community/gathering/{id}", "/community/question",
                "/community/question/{id}", "/wish", "/{articleId}/comment",
                "/{articleId}/comment/{commentId}/recomment");
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
