package org.protobeans.mvc.config;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvc.controller.advice.InitBinderControllerAdvice;
import org.protobeans.mvc.util.FileUtils;
import org.protobeans.mvc.util.FilterBean;
import org.protobeans.mvc.util.GlobalModelAttribute;
import org.protobeans.mvc.util.PathUtils;
import org.protobeans.mvc.util.ProtobeansMessageInterpolator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableWebMvc
@Configuration
@InjectFrom(EnableMvc.class)
@ComponentScan(basePackageClasses = InitBinderControllerAdvice.class)
public class MvcConfig implements WebMvcConfigurer {
    private String resourcesPath;
    
    private String resourcesUrl;
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired(required = false)
    private List<HttpMessageConverter<?>> converters = new ArrayList<>();
    
    @Autowired(required = false)
    private FilterBean filterBean;
    
    @Bean
    public Class<? extends WebApplicationInitializer> mvcInitializer(ConfigurableWebApplicationContext ctx) {
        MvcInitializer.rootApplicationContext = ctx;
        
        if (filterBean != null) {
            MvcInitializer.filters = filterBean.getFilters();
        }
        
        return MvcInitializer.class; 
    }
    
    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
                                 .setVisibility(PropertyAccessor.GETTER, Visibility.NONE)
                                 .setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE)
                                 .setVisibility(PropertyAccessor.SETTER, Visibility.NONE)
                                 .setVisibility(PropertyAccessor.CREATOR, Visibility.NONE)
                                 .setSerializationInclusion(Include.NON_EMPTY)
                                 .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    @Bean
    HttpMessageConverter<?> jacksonMessageConverter() {
        return new MappingJackson2HttpMessageConverter(mapper());
    }
    
    @Bean
    HttpMessageConverter<?> stringMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }
    
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }
    
    @Override
    public Validator getValidator() {
        return localValidatorFactoryBean();
    }

    @Bean
    LocalValidatorFactoryBean localValidatorFactoryBean() {
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
        validatorFactoryBean.setMessageInterpolator(new ProtobeansMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource), cached));
        
        return validatorFactoryBean;
    }
    
    @Bean
    public GlobalModelAttribute globalModelAttribute() {
        String value = null;
        
        if (!resourcesPath.isEmpty() && !resourcesUrl.isEmpty()) {
            String dashedResourcesPath = PathUtils.dashedPath(resourcesPath);
            String dashedResourcesUrl = PathUtils.dashedPath(resourcesUrl);
            long lastModified = FileUtils.getLastModified(dashedResourcesPath);
            
            value = dashedResourcesUrl + lastModified;
        }
        
        return new GlobalModelAttribute("resourcesPrefix", value);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!resourcesPath.isEmpty() && !resourcesUrl.isEmpty()) {
            String dashedResourcesPath = PathUtils.dashedPath(resourcesPath);
            String dashedResourcesUrl = PathUtils.dashedPath(resourcesUrl);
            long lastModified = FileUtils.getLastModified(dashedResourcesPath);
        
            registry.addResourceHandler(dashedResourcesUrl + lastModified + "/**")
                    .addResourceLocations("classpath:" + dashedResourcesPath)
                    .setCachePeriod(31556926);
        }
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> defaultConverters) {
        defaultConverters.addAll(converters);
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

