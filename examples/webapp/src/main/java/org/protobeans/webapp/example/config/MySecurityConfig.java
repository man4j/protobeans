package org.protobeans.webapp.example.config;

import org.protobeans.webapp.example.security.TokenFormRouterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authorization.EnableMultiFactorAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMultiFactorAuthentication(authorities = {FactorGrantedAuthority.PASSWORD_AUTHORITY, FactorGrantedAuthority.OTT_AUTHORITY})
public class MySecurityConfig {
    @Bean
    Customizer<HttpSecurity> myCustomizer() {
        return (http) -> http.addFilterBefore(new TokenFormRouterFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
