package org.protobeans.security.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;

import org.protobeans.mvc.util.PathUtils;
import org.protobeans.security.advice.SecurityControllerAdvice;
import org.protobeans.security.annotation.Anonymous;
import org.protobeans.security.annotation.PermitAll;
import org.protobeans.security.service.SecurityService;
import org.protobeans.security.util.CurrentUrlAuthenticationSuccessHandler;
import org.protobeans.security.util.FilterChainBean;
import org.protobeans.security.util.SecurityUrlsBean;
import org.protobeans.security.validation.SignIn;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses={SecurityService.class, SecurityControllerAdvice.class, SignIn.class})
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private ApplicationContext ctx;
    
    @Autowired
    private SecurityUrlsBean securityUrlsBean;
    
    @Autowired(required = false)
    private FilterChainBean filterChainBean;
    
    @Autowired(required = false)
    private List<SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>> configurers = new ArrayList<>();
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(Arrays.stream(securityUrlsBean.getIgnoreUrls())
                                         .map(u -> PathUtils.dashedPath(u) + "**")
                                         .toArray(String[]::new));
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {        
        String[] anonymousPatterns = ctx.getBeansWithAnnotation(Anonymous.class).values().stream().map(o -> AopUtils.getTargetClass(o).getAnnotation(Anonymous.class).value()).toArray(String[]::new);
        String[] permitAllPatterns = ctx.getBeansWithAnnotation(PermitAll.class).values().stream().map(o -> AopUtils.getTargetClass(o).getAnnotation(PermitAll.class).value()).toArray(String[]::new);
        
        http.authorizeRequests().mvcMatchers(permitAllPatterns).permitAll()
                                .mvcMatchers(anonymousPatterns).anonymous()
                                .anyRequest().authenticated()
            .and()
            .rememberMe().rememberMeServices(rememberMeServices()).key("123").authenticationSuccessHandler(new CurrentUrlAuthenticationSuccessHandler())
            .and()
            .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(securityUrlsBean.getLoginUrl())).accessDeniedHandler((req, res, e) -> {res.setStatus(HttpServletResponse.SC_FORBIDDEN);});
        
        for (SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> cfg : configurers) {
            http.apply(cfg);
        }
        
        http.addFilterBefore(new CharacterEncodingFilter(StandardCharsets.UTF_8.displayName(), true, true), ChannelProcessingFilter.class);
        
        if (filterChainBean != null) {
            for (Filter filter : filterChainBean.getFilters()) {
                http.addFilterBefore(filter, ChannelProcessingFilter.class);
            }
        }
    }
    
    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices("123", userDetailsService);
    }
}
