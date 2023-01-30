package org.protobeans.security.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.mvc.util.PathUtils;
import org.protobeans.security.advice.SecurityControllerAdvice;
import org.protobeans.security.annotation.Anonymous;
import org.protobeans.security.annotation.DisableCsrf;
import org.protobeans.security.annotation.EnableSecurity;
import org.protobeans.security.annotation.PermitAll;
import org.protobeans.security.service.SecurityService;
import org.protobeans.security.util.CurrentUrlAuthenticationSuccessHandler;
import org.protobeans.security.validation.CurrentPassword;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@InjectFrom(EnableSecurity.class)
@ComponentScan(basePackageClasses={SecurityService.class, SecurityControllerAdvice.class, CurrentPassword.class})
public class SecurityConfig {
    private String[] ignoreUrls;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private ApplicationContext ctx;
    
    @Autowired(required = false)
    private PermissionEvaluator permissionEvaluator;
    
    private boolean disableCsrf; 
    
    @Autowired(required = false)
    private List<AbstractHttpConfigurer<?, HttpSecurity>> securityDsl = new ArrayList<>();
    
    @Autowired
    private List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        
        builder.authenticationEventPublisher(new DefaultAuthenticationEventPublisher())
               .eraseCredentials(false)
               .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

        for (var provider : authenticationProviders) {
            builder.authenticationProvider(provider);
        }
        
        return builder.build();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {     
        String[] anonymousPatterns = ctx.getBeansWithAnnotation(Anonymous.class).values().stream().map(o -> AopUtils.getTargetClass(o).getAnnotation(Anonymous.class).value()).toArray(String[]::new);
        String[] permitAllPatterns = ctx.getBeansWithAnnotation(PermitAll.class).values().stream().map(o -> AopUtils.getTargetClass(o).getAnnotation(PermitAll.class).value()).toArray(String[]::new);
        String[] disableCsrfPatterns = ctx.getBeansWithAnnotation(DisableCsrf.class).values().stream().map(o -> AopUtils.getTargetClass(o).getAnnotation(DisableCsrf.class).mvcPattern()).toArray(String[]::new);
        
        http.authenticationManager(authenticationManager(http))
            .authorizeHttpRequests().requestMatchers(permitAllPatterns).permitAll()
                                    .requestMatchers("/favicon.ico", "/swagger-ui.html/**", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/webjars/**", "/csrf").permitAll()
                                    .requestMatchers(anonymousPatterns).permitAll()
                                    .requestMatchers(Arrays.stream(ignoreUrls).map(u -> PathUtils.dashedPath(u) + "**").toArray(String[]::new)).permitAll()
                                    .anyRequest().authenticated()
            .and().rememberMe().rememberMeServices(rememberMeServices()).key("123").authenticationSuccessHandler(new CurrentUrlAuthenticationSuccessHandler())
            .and().securityContext().requireExplicitSave(false)
            .and().sessionManagement().requireExplicitAuthenticationStrategy(false).sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            .and().addFilterBefore(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true, true), ChannelProcessingFilter.class);
        
        if (disableCsrf) {
            http.csrf().disable();
        } else {
            http.csrf().ignoringRequestMatchers(disableCsrfPatterns);
        }
        
        for (var configurer : securityDsl) {
            http.apply(configurer);
        }
        
        return http.build();
    }
    
    @Bean
    public Class<? extends WebApplicationInitializer> securityInitializer() {
        return SecurityInitializer.class;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices("123", userDetailsService);
    }
    
    @Bean
    public MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler defaultMethodExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        
        if (permissionEvaluator != null) {
            defaultMethodExpressionHandler.setPermissionEvaluator(permissionEvaluator);
            defaultMethodExpressionHandler.setApplicationContext(ctx);
        }
        
        return defaultMethodExpressionHandler;
    }
}
