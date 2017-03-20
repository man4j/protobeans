package org.protobeans.mvc.config;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.protobeans.mvc.controller.advice.InitBinderControllerAdvice;
import org.protobeans.mvc.util.FreeMarkerVariables;
import org.protobeans.mvc.util.MessageConvertersBean;
import org.protobeans.mvc.util.ProtoBeansDefinitionScanner;
import org.protobeans.mvc.util.ResourcesVersionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = InitBinderControllerAdvice.class)
public class DispatcherServletContextConfig extends WebMvcConfigurerAdapter {
    public static WebApplicationContext webApplicationContext;
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private ResourcesVersionBean resourcesVersionBean;
    
    @Autowired(required = false)
    private MessageConvertersBean messageConvertersBean;
    
    @Autowired
    private ConfigurableApplicationContext ctx;
    
    @Autowired
    private ProtoBeansDefinitionScanner protoBeansDefinitionScanner;
    
    @PostConstruct
    public void init() {
        webApplicationContext = (WebApplicationContext) ctx;
        protoBeansDefinitionScanner.scan(ctx);
    }
    
    @Bean
    public FreeMarkerVariables freeMarkerVariables() {
        FreeMarkerVariables freeMarkerVariables = new FreeMarkerVariables();
        
        freeMarkerVariables.setVariable("resourcesPrefix", resourcesVersionBean.getResourcesUrl() + resourcesVersionBean.getLastModified());
        
        return freeMarkerVariables;
    }
    
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        
        boolean cached = true;
        
        if (messageSource instanceof DelegatingMessageSource) {
            DelegatingMessageSource dms = (DelegatingMessageSource) messageSource;

            MessageSource parentMessageSource = dms.getParentMessageSource();

            while (parentMessageSource instanceof DelegatingMessageSource) {
                parentMessageSource = ((DelegatingMessageSource) parentMessageSource).getParentMessageSource();
            }
            
            if (parentMessageSource instanceof ReloadableResourceBundleMessageSource) {
                ReloadableResourceBundleMessageSource rms = (ReloadableResourceBundleMessageSource) parentMessageSource;

                Field f = ReflectionUtils.findField(ReloadableResourceBundleMessageSource.class, "cacheMillis");
                
                f.setAccessible(true);
                
                try {
                    long millis = f.getLong(rms);
                    
                    cached = (millis == -1);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        
        //validatorFactoryBean.setValidationMessageSource(messageSource);
        validatorFactoryBean.setMessageInterpolator(new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource), cached));
        
        return validatorFactoryBean;
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcesVersionBean.getResourcesUrl() + resourcesVersionBean.getLastModified() + "/**")
                                                        .addResourceLocations("classpath:" + resourcesVersionBean.getResourcesPath())
                                                        .setCachePeriod(31556926);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        if (messageConvertersBean != null) {
            converters.addAll(messageConvertersBean.getConverters());
        }
    }
        
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
    } 
    
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.US);
        return resolver;
    } 
    
    @Bean
    public StandardServletMultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }
}
