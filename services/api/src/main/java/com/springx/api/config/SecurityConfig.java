package com.springx.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.security.oidc.issuer-uri:}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/api/health", "/actuator/health", "/error").permitAll();
                if (issuerUri.isBlank()) {
                    auth.anyRequest().denyAll();
                } else {
                    auth.anyRequest().authenticated();
                }
            })
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable);
        if (!issuerUri.isBlank()) {
            http.oauth2ResourceServer(oauth2 -> oauth2.jwt());
        }
        return http.build();
    }
}
