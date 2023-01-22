package org.protobeans.webapp.example.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.ForwardLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfigurer extends AbstractHttpConfigurer<SecurityConfigurer, HttpSecurity> {
    @Override
    public void init(HttpSecurity http) throws Exception {
        LoginUrlAuthenticationEntryPoint authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/signin");
        authenticationEntryPoint.setUseForward(true);
        
        http.logout().logoutSuccessHandler(new ForwardLogoutSuccessHandler("/signin"));
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedPage("/");
    }
}
