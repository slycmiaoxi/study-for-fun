package com.study.fun.portal.config;

import com.study.fun.portal.filter.OauthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class FilterConfig {

    @Autowired
    @Lazy
    private OauthFilter oauthFilter;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

   @Bean
    public FilterRegistrationBean filter1() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(oauthFilter);
        registration.addUrlPatterns("/*");
        registration.setName("oauthFilter");
        //设置优先级别
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
