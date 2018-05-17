package org.protobeans.security.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.core.util.LambdaExceptionUtil;
import org.protobeans.mvc.util.PathUtils;
import org.protobeans.security.advice.SecurityControllerAdvice;
import org.protobeans.security.annotation.Anonymous;
import org.protobeans.security.annotation.EnableSecurity;
import org.protobeans.security.annotation.PermitAll;
import org.protobeans.security.auth.UuidAuthenticationProvider;
import org.protobeans.security.service.SecurityService;
import org.protobeans.security.util.CurrentUrlAuthenticationSuccessHandler;
import org.protobeans.security.validation.CurrentPassword;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.WebApplicationInitializer;

@Configuration
@EnableWebSecurity
@InjectFrom(EnableSecurity.class)
@ComponentScan(basePackageClasses={SecurityService.class, SecurityControllerAdvice.class, CurrentPassword.class})
@Order(10)//для того, чтобы фильтры SpringSecurity инициализировались позже фильтров WebMVC
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private String[] ignoreUrls;
    
    private String loginUrl;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private ApplicationContext ctx;
    
    @Autowired(required = false)
    private List<SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>> configurers = new ArrayList<>();
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(Arrays.stream(ignoreUrls).map(u -> PathUtils.dashedPath(u) + "**").toArray(String[]::new));
    }
    
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        AuthenticationManager authenticationManager = super.authenticationManager();
        
        //иногда текущие credentials нужны, например в форме изменения пароля
        ((ProviderManager) authenticationManager).setEraseCredentialsAfterAuthentication(false);
        
        return authenticationManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {     
        String[] anonymousPatterns = ctx.getBeansWithAnnotation(Anonymous.class).values().stream().map(o -> AopUtils.getTargetClass(o).getAnnotation(Anonymous.class).value()).toArray(String[]::new);
        String[] permitAllPatterns = ctx.getBeansWithAnnotation(PermitAll.class).values().stream().map(o -> AopUtils.getTargetClass(o).getAnnotation(PermitAll.class).value()).toArray(String[]::new);
        
        http.authenticationProvider(new UuidAuthenticationProvider())//add custom provider
            .authorizeRequests().mvcMatchers(permitAllPatterns).permitAll().mvcMatchers(anonymousPatterns).anonymous().anyRequest().authenticated()
            .and().rememberMe().rememberMeServices(rememberMeServices()).key("123").authenticationSuccessHandler(new CurrentUrlAuthenticationSuccessHandler())
            .and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(loginUrl))
                                      .accessDeniedHandler((req, res, e) -> {res.setStatus(HttpServletResponse.SC_FORBIDDEN);});
        
        configurers.forEach(LambdaExceptionUtil.rethrowConsumer(http::apply));
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
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
}
