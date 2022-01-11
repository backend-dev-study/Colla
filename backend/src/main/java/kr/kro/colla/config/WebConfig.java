package kr.kro.colla.config;

import kr.kro.colla.auth.presentation.argument_resolver.AuthArgumentResolver;
import kr.kro.colla.auth.presentation.interceptor.AuthInterceptor;
import kr.kro.colla.auth.service.AuthService;
import kr.kro.colla.utils.CookieManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed_origin}")
    private String origin;

    @Value("${cookie.domain}")
    private String credentialDomain;

    @Value("${cookie.expiration_time}")
    private long expirationTime;

    private final AuthService authService;

    @Bean
    public CookieManager cookieManager() {
        return new CookieManager(credentialDomain, expirationTime);
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor(authService, cookieManager());
    }

    @Bean
    public AuthArgumentResolver authArgumentResolver() {
        return new AuthArgumentResolver(authService);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(origin)
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/login");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver());
    }
}
