package org.protobeans.mvc.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.jspecify.annotations.Nullable;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.exchange.ProtobeansHttpInterfaceUtils;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvc.controller.advice.ModelControllerAdvice;
import org.protobeans.mvc.util.FileUtils;
import org.protobeans.mvc.util.FilterBean;
import org.protobeans.mvc.util.GlobalModelAttribute;
import org.protobeans.mvc.util.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
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

import jakarta.annotation.PostConstruct;
import tools.jackson.databind.json.JsonMapper;


@EnableWebMvc
@Configuration
@InjectFrom(EnableMvc.class)
@ComponentScan(basePackageClasses = {ModelControllerAdvice.class})
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class MvcConfig implements WebMvcConfigurer {
    private String resourcesPath;
    
    private String resourcesUrl;
    
    private String sessionCookieName;
    
    private String cacheMessageSource;
    
    public static String _resourcesUrl;
        
    @Autowired(required = false)
    private FilterBean filterBean;
    
    @Autowired(required = false)
    private List<HandlerInterceptor> interceptors = new ArrayList<>();
    
    @PostConstruct
    public void init() {
        MvcInitializer.sessionCookieName = sessionCookieName;
        _resourcesUrl = resourcesUrl;
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        var dashedResourcesPath = PathUtils.dashedPath(resourcesPath);
        var dashedResourcesUrl = PathUtils.dashedPath(resourcesUrl);
        var lastModified = FileUtils.getLastModified(dashedResourcesPath);
        
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(31556926);
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(31556926);
        registry.addResourceHandler(dashedResourcesUrl + lastModified + "/**").addResourceLocations("classpath:" + dashedResourcesPath).setCachePeriod(31556926);
    }
    
    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
        builder.registerDefaults()
               .withStringConverter(new StringHttpMessageConverter(StandardCharsets.UTF_8))
               .withJsonConverter(new JacksonJsonHttpMessageConverter(mapper())).build();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
        interceptors.forEach(registry::addInterceptor);
    }
    
    @Override
    public @Nullable Validator getValidator() {
        return localValidatorFactoryBean();
    }
    
    @Bean
    public Class<? extends WebApplicationInitializer> mvcInitializer(ConfigurableWebApplicationContext ctx) {
        MvcInitializer.rootApplicationContext = ctx;
        
        if (filterBean != null) {
            MvcInitializer.filters = filterBean.getFilters();
        }
        
        return MvcInitializer.class; 
    }

    @Bean
    public GlobalModelAttribute globalModelAttribute() {
        var dashedResourcesPath = PathUtils.dashedPath(resourcesPath);
        var dashedResourcesUrl = PathUtils.dashedPath(resourcesUrl);
        var lastModified = FileUtils.getLastModified(dashedResourcesPath);
        
        return new GlobalModelAttribute("resourcesPrefix", dashedResourcesUrl + lastModified);
    }
    
    @Bean
    public JsonMapper mapper() {
        return ProtobeansHttpInterfaceUtils.mapper();
    }
    
    @Bean
    public LocaleResolver localeResolver() {
        var resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.of("ru", "RU"));
        return resolver;
    } 
    
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
    
    @Bean
    public static MethodValidationPostProcessor methodValidationPostProcessor(LocalValidatorFactoryBean validator) {
        var methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidator(validator);
        return methodValidationPostProcessor;
    }
    
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public MessageSource messageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds( Boolean.valueOf(cacheMessageSource) ? -1 : 0);
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }
    
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Primary
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        var validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setMessageInterpolator(new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource()), 
                Boolean.valueOf(cacheMessageSource)));
        return validatorFactoryBean;
    }
}
