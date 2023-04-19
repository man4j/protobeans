package org.protobeans.webapp.example.security;

import org.protobeans.mvc.rest.model.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.ForwardLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityConfigurer extends AbstractHttpConfigurer<SecurityConfigurer, HttpSecurity> {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void init(HttpSecurity http) throws Exception {
        LoginUrlAuthenticationEntryPoint authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/signin");
        authenticationEntryPoint.setUseForward(true);
        
        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint() {
            @SuppressWarnings("resource")
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws java.io.IOException {
                RestResult restResult = new RestResult("Unauthorized");
                response.setStatus(401);
                response.getWriter().write(mapper.writeValueAsString(restResult));
            }
        };
        
        basicAuthenticationEntryPoint.setRealmName("API");
        
        http.logout().logoutSuccessHandler(new ForwardLogoutSuccessHandler("/signin"));
        http.httpBasic().authenticationEntryPoint(basicAuthenticationEntryPoint).realmName("API");
        http.exceptionHandling().defaultAuthenticationEntryPointFor(basicAuthenticationEntryPoint, new AntPathRequestMatcher("/api/**"))
                                .defaultAuthenticationEntryPointFor(authenticationEntryPoint, new AntPathRequestMatcher("/**")).accessDeniedPage("/forbidden");
        
    }
}
