package org.protobeans.webapp.example.security;

import java.io.IOException;

import org.protobeans.mvc.rest.model.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityConfigurer extends AbstractHttpConfigurer<SecurityConfigurer, HttpSecurity> {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void init(HttpSecurity http) throws Exception {
        LoginUrlAuthenticationEntryPoint uiAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/signin");
        uiAuthenticationEntryPoint.setUseForward(true);
        
        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint() {
            @SuppressWarnings("resource")
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws java.io.IOException {
                RestResult restResult = new RestResult(HttpStatus.UNAUTHORIZED.getReasonPhrase());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(mapper.writeValueAsString(restResult));
            }
        };
        
        basicAuthenticationEntryPoint.setRealmName("API");
        
        http.httpBasic(c -> {
            c.authenticationEntryPoint(basicAuthenticationEntryPoint).realmName("API");
        }).exceptionHandling(c -> {
            AccessDeniedHandlerImpl uiHandler = new AccessDeniedHandlerImpl();
            uiHandler.setErrorPage("/forbidden");
            
            AccessDeniedHandler basicHandler = new AccessDeniedHandler() {
                @SuppressWarnings("resource")
                @Override
                public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                    RestResult restResult = new RestResult(HttpStatus.FORBIDDEN.getReasonPhrase());
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write(mapper.writeValueAsString(restResult));
                }
            };
            
            c.defaultAccessDeniedHandlerFor(basicHandler, new AntPathRequestMatcher("/api/**"))
             .defaultAccessDeniedHandlerFor(uiHandler, new AntPathRequestMatcher("/**"))           
             .defaultAuthenticationEntryPointFor(basicAuthenticationEntryPoint, new AntPathRequestMatcher("/api/**"))
             .defaultAuthenticationEntryPointFor(uiAuthenticationEntryPoint, new AntPathRequestMatcher("/**"));
        });
    }
}
