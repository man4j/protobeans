package org.protobeans.mvc.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.exchange.ProtobeansHttpInterfaceUtils;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvc.controller.advice.ModelControllerAdvice;
import org.protobeans.mvc.rest.advice.ExceptionControllerAdvice;
import org.protobeans.mvc.util.FileUtils;
import org.protobeans.mvc.util.FilterBean;
import org.protobeans.mvc.util.GlobalModelAttribute;
import org.protobeans.mvc.util.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;


@EnableWebMvc
@Configuration
@InjectFrom(EnableMvc.class)
@Import(MvcValidatorConfig.class)
@ComponentScan(basePackageClasses= {ModelControllerAdvice.class, ExceptionControllerAdvice.class})
public class MvcConfig implements WebMvcConfigurer {
    private String resourcesPath = "static";
    
    public static final String resourcesUrl = "static";
    
    private String sessionCookieName;
        
    @Autowired(required = false)
    private FilterBean filterBean;
    
    @Autowired(required = false)
    private List<HandlerInterceptor> interceptors = new ArrayList<>();
    
    @Autowired
    private LocalValidatorFactoryBean localValidatorFactoryBean;
    
    @Bean
    public Class<? extends WebApplicationInitializer> mvcInitializer(ConfigurableWebApplicationContext ctx) {
        MvcInitializer.rootApplicationContext = ctx;
        MvcInitializer.sessionCookieName = sessionCookieName;
        
        if (filterBean != null) {
            MvcInitializer.filters = filterBean.getFilters();
        }
        
        return MvcInitializer.class; 
    }
    
    @Override
    public Validator getValidator() {
        return localValidatorFactoryBean;
    }

    @Bean
    public GlobalModelAttribute globalModelAttribute() {
        String value = null;
        
        String dashedResourcesPath = PathUtils.dashedPath(resourcesPath);
        String dashedResourcesUrl = PathUtils.dashedPath(resourcesUrl);
        long lastModified = FileUtils.getLastModified(dashedResourcesPath);
        
        value = dashedResourcesUrl + lastModified;
        
        return new GlobalModelAttribute("resourcesPrefix", value);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dashedResourcesPath = PathUtils.dashedPath(resourcesPath);
        String dashedResourcesUrl = PathUtils.dashedPath(resourcesUrl);
        
        long lastModified = FileUtils.getLastModified(dashedResourcesPath);
        
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(31556926);
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(31556926);
        registry.addResourceHandler(dashedResourcesUrl + lastModified + "/**").addResourceLocations("classpath:" + dashedResourcesPath).setCachePeriod(31556926);
    }
    
    //=============================================================================
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> defaultConverters) {
        defaultConverters.addAll(ProtobeansHttpInterfaceUtils.protobeansConverters());
    }
    
    @Bean
    public ObjectMapper mapper() {
        return ProtobeansHttpInterfaceUtils.mapper();
    }
    
    //=============================================================================
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
        
        interceptors.forEach(registry::addInterceptor);
    }
    
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.of("ru", "RU"));
        return resolver;
    } 
    
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
