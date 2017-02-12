package org.protobeans.security.config;

import java.util.Arrays;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.protobeans.mvc.util.PathUtils;
import org.protobeans.security.annotation.Anonymous;
import org.protobeans.security.annotation.PermitAll;
import org.protobeans.security.service.SecurityService;
import org.protobeans.security.util.CurrentUrlAuthenticationSuccessHandler;
import org.protobeans.security.util.SecurityUrlsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses=SecurityService.class)
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private ApplicationContext ctx;
    
    @Autowired
    private SecurityUrlsBean securityUrlsBean;
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(Arrays.stream(securityUrlsBean.getIgnoreUrls())
                                         .map(u -> PathUtils.dashedPath(u) + "**")
                                         .toArray(String[]::new));
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {        
        String[] anonymousPatterns = ctx.getBeansWithAnnotation(Anonymous.class).values().stream().map(o -> o.getClass().getAnnotation(Anonymous.class).value()).toArray(String[]::new);
        String[] permitAllPatterns = ctx.getBeansWithAnnotation(PermitAll.class).values().stream().map(o -> o.getClass().getAnnotation(PermitAll.class).value()).toArray(String[]::new);
            
        http.authorizeRequests().mvcMatchers(permitAllPatterns).permitAll()
                                .mvcMatchers(anonymousPatterns).anonymous()
                                .anyRequest().authenticated()
            .and()
            .rememberMe().rememberMeServices(rememberMeServices()).authenticationSuccessHandler(new CurrentUrlAuthenticationSuccessHandler())
            .and()
            .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(securityUrlsBean.getLoginUrl())).accessDeniedHandler((req, res, e) -> {res.setStatus(HttpServletResponse.SC_FORBIDDEN);})
            .and()
            .csrf().disable();
    }
    
    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices(UUID.randomUUID().toString(), userDetailsService);
    }
}
