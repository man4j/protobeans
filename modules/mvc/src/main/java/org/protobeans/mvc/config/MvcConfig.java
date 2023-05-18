package org.protobeans.mvc.config;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.mvc.annotation.EnableMvc;
import org.protobeans.mvc.util.FileUtils;
import org.protobeans.mvc.util.FilterBean;
import org.protobeans.mvc.util.GlobalModelAttribute;
import org.protobeans.mvc.util.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@EnableWebMvc
@Configuration
@InjectFrom(EnableMvc.class)
@Import(MvcValidatorConfig.class)
@ComponentScan(basePackages = "org.protobeans.mvc.controller")
public class MvcConfig implements WebMvcConfigurer {
    private String resourcesPath = "static";
    
    public static final String resourcesUrl = "static";
    
    private String sessionCookieName;
        
    @Autowired(required = false)
    private FilterBean filterBean;
    
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
    
    @Bean
    public ObjectMapper mapper() {
        return JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                   .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, true)
                                   .visibility(PropertyAccessor.FIELD, Visibility.ANY)
                                   .visibility(PropertyAccessor.GETTER, Visibility.NONE)
                                   .visibility(PropertyAccessor.IS_GETTER, Visibility.NONE)
                                   .visibility(PropertyAccessor.SETTER, Visibility.NONE)
                                   .visibility(PropertyAccessor.CREATOR, Visibility.NONE)
                                   .serializationInclusion(Include.NON_NULL)
                                   .build()
                                   .registerModule(new JavaTimeModule())
                                   .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    @Bean
    HttpMessageConverter<?> jacksonMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(mapper());
        converter.setSupportedMediaTypes(List.of(new MediaType("application", "octet-stream"), MediaType.APPLICATION_JSON, new MediaType("application", "*+json")));
        return converter;
    }
    
    @Bean
    HttpMessageConverter<?> stringMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
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
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> defaultConverters) {
        defaultConverters.add(stringMessageConverter());
        defaultConverters.add(jacksonMessageConverter());
        defaultConverters.add(new ByteArrayHttpMessageConverter());
        defaultConverters.add(new ResourceHttpMessageConverter());
    }
        
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
    }
    
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale("ru", "RU"));
        return resolver;
    } 
    
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}

