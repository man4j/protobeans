package org.protobeans.security.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.mvc.config.MvcConfig;
import org.protobeans.mvc.util.PathUtils;
import org.protobeans.security.advice.SecurityControllerAdvice;
import org.protobeans.security.annotation.Anonymous;
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
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@InjectFrom(EnableSecurity.class)
@ComponentScan(basePackageClasses={SecurityService.class, SecurityControllerAdvice.class, CurrentPassword.class})
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true, proxyTargetClass = true)
public class SecurityConfig {
    private String[] ignoreUrls = new String[] {MvcConfig.resourcesUrl};
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private ApplicationContext ctx;
    
    @Autowired(required = false)
    private PermissionEvaluator permissionEvaluator;
    
    @Autowired(required = false)
    private List<AbstractHttpConfigurer<?, HttpSecurity>> securityDsl = new ArrayList<>();
    
    @Autowired(required = false)
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
        
        http.authorizeHttpRequests(c -> {
            c.requestMatchers(permitAllPatterns).permitAll()
             .requestMatchers("/favicon.ico", "/swagger-ui.html/**", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/webjars/**", "/csrf").permitAll()
             .requestMatchers(anonymousPatterns).anonymous()
             .requestMatchers(Arrays.stream(ignoreUrls).map(u -> PathUtils.dashedPath(u) + "**").toArray(String[]::new)).permitAll()
             .anyRequest().authenticated();
        }).rememberMe(c -> {
            c.rememberMeServices(rememberMeServices()).key("123").authenticationSuccessHandler(new CurrentUrlAuthenticationSuccessHandler());
        }).securityContext(c -> {
            c.requireExplicitSave(false);
        }).sessionManagement(c -> {
            c.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        }).addFilterBefore(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true, true), ChannelProcessingFilter.class)
        .authenticationManager(authenticationManager(http));

//        .httpBasic(c -> {
//            c.authenticationDetailsSource(null)
//             .authenticationEntryPoint(null)
//             .realmName(null);
//        }).exceptionHandling(c -> {
//            c.accessDeniedHandler(null)
//             .accessDeniedPage(null)
//             .authenticationEntryPoint(null)
//             .defaultAccessDeniedHandlerFor(null, null)
//             .defaultAuthenticationEntryPointFor(null, null)
//        }).logout(c -> {
//            c.addLogoutHandler(null)
//             .clearAuthentication(true)
//             .deleteCookies(permitAllPatterns)
//             .invalidateHttpSession(true)
//             .logoutRequestMatcher(null)
//             .logoutSuccessHandler(null)
//             .logoutSuccessUrl(null)
//             .logoutUrl(null)
//             .permitAll(true);
//        });

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
    
    @Bean
    public EvaluationContextExtension securityExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
