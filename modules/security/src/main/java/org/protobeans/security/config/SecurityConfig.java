package org.protobeans.security.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.mvc.config.MvcConfig;
import org.protobeans.mvc.util.PathUtils;
import org.protobeans.security.annotation.Anonymous;
import org.protobeans.security.annotation.EnableSecurity;
import org.protobeans.security.annotation.PermitAll;
import org.protobeans.security.controller.advice.SecurityModelControllerAdvice;
import org.protobeans.security.model.AuthenticationProviderWrapper;
import org.protobeans.security.service.ProfileService;
import org.protobeans.security.service.ProtobeansUserDetailsService;
import org.protobeans.security.util.CurrentUrlAuthenticationSuccessHandler;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.DefaultAuthorizationManagerFactory;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import jakarta.servlet.http.HttpServletRequest;


@Configuration
@InjectFrom(EnableSecurity.class)
@ComponentScan(basePackageClasses={SecurityModelControllerAdvice.class})
public class SecurityConfig {
    @Autowired
    private ApplicationContext ctx;
    
    @Autowired(required = false)
    private List<AuthenticationProviderWrapper> authenticationProviders = new ArrayList<>();
    
    @Autowired(required = false)
    private List<Customizer<HttpSecurity>> customizers = new ArrayList<>();
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ProfileService profileService;
    
    private String loginFormUrl;
    
    private String accessDeniedUrl;
    
    private String rememberMeKey;
        
    @Bean
    public AuthenticationManager authenticationManager() {
        var providers = authenticationProviders.stream()
                                               .map(AuthenticationProviderWrapper::getProvider)
                                               .collect(Collectors.toCollection(ArrayList::new));
        
        var daoProvider = new DaoAuthenticationProvider(new ProtobeansUserDetailsService(profileService));
        daoProvider.setPasswordEncoder(passwordEncoder);
        
        providers.add(daoProvider);

        var pm = new ProviderManager(providers);
        
        pm.setEraseCredentialsAfterAuthentication(false);
        pm.setAuthenticationEventPublisher(new DefaultAuthenticationEventPublisher());
        
        return pm;
    }
    
    @Bean
    @Order(50)
    public SecurityFilterChain resourcesFilterChain(HttpSecurity http) throws Exception {
        var resourceUrl = PathUtils.dashedPath(MvcConfig._resourcesUrl) + "**";
        
        http.securityMatcher(resourceUrl)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .requestCache(cache -> cache.disable())
            .securityContext(с -> с.disable())
            .sessionManagement(sm -> sm.disable())
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
    
    @Bean
    @Order(100)
    public SecurityFilterChain mvcChain(HttpSecurity http) throws Exception {     
        var anonymousPatterns = ctx.getBeansWithAnnotation(Anonymous.class).values().stream().map(o -> AopUtils.getTargetClass(o).getAnnotation(Anonymous.class).value()).toArray(String[]::new);
        var permitAllPatterns = ctx.getBeansWithAnnotation(PermitAll.class).values().stream().map(o -> AopUtils.getTargetClass(o).getAnnotation(PermitAll.class).value()).toArray(String[]::new);
        
        http.authorizeHttpRequests(c ->
                c.requestMatchers(permitAllPatterns).permitAll()
                 .requestMatchers("/favicon.ico", "/swagger-ui.html/**", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/webjars/**", "/csrf").permitAll()
                 .requestMatchers(anonymousPatterns).access(new DefaultAuthorizationManagerFactory<>().anonymous())
                 .anyRequest().authenticated())
            .rememberMe(c -> c.rememberMeServices(rememberMeServices()).authenticationSuccessHandler(new CurrentUrlAuthenticationSuccessHandler()))
            .securityContext(c -> c.requireExplicitSave(false))
            .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
            .exceptionHandling(c ->
                c.accessDeniedPage(accessDeniedUrl)
                 .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(loginFormUrl)));

        customizers.forEach(c -> c.customize(http));
        
        return http.build();
    }
    
    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        var rememberMeServices = new TokenBasedRememberMeServices(rememberMeKey, new ProtobeansUserDetailsService(profileService)) {
            @Override
            protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
                var grantedAuthorities = new ArrayList<GrantedAuthority>(user.getAuthorities());
                grantedAuthorities.add(FactorGrantedAuthority.fromAuthority(FactorGrantedAuthority.PASSWORD_AUTHORITY));
                var auth = new RememberMeAuthenticationToken(rememberMeKey, user, grantedAuthorities);
                auth.setDetails(getAuthenticationDetailsSource().buildDetails(request)); // важно
                return auth;
            }
        };
        
        return rememberMeServices;
    }
}
