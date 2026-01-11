package org.protobeans.security.config;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.exchange.model.RestResult;
import org.protobeans.mvc.rest.advice.RestResultControllerAdvice;
import org.protobeans.security.annotation.EnableRestSecurity;
import org.protobeans.security.annotation.PermitAll;
import org.protobeans.security.rest.SecurityRestResultControllerAdvice;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.DefaultAuthorizationManagerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.json.JsonMapper;


@Configuration
@InjectFrom(EnableRestSecurity.class)
@ComponentScan(basePackageClasses={SecurityRestResultControllerAdvice.class, RestResultControllerAdvice.class})
public class RestSecurityConfig {
    @Autowired
    private ApplicationContext ctx;
    
    @Autowired
    private JsonMapper mapper;

    @Bean
    @Order(60)
    public SecurityFilterChain restFilterChain(HttpSecurity http) throws Exception {
        var permitAllPatterns = ctx.getBeansWithAnnotation(PermitAll.class).values().stream().map(o -> AopUtils.getTargetClass(o).getAnnotation(PermitAll.class).value()).toArray(String[]::new);

        var basicEntryPoint = new BasicAuthenticationEntryPoint() {
            @SuppressWarnings("resource")
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws java.io.IOException {
                RestResult restResult = new RestResult(HttpStatus.UNAUTHORIZED.getReasonPhrase());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(mapper.writeValueAsString(restResult));
            }
        };
        
        basicEntryPoint.setRealmName("API");
        
        http.securityMatcher("/api/**")
            .authorizeHttpRequests(c -> 
                c.requestMatchers(permitAllPatterns).permitAll()
                 .anyRequest().access(new DefaultAuthorizationManagerFactory<>().authenticated()))
            .httpBasic(c -> c.authenticationEntryPoint(basicEntryPoint))
            .securityContext(c -> c.requireExplicitSave(false))
            .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
