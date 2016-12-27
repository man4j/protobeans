package org.protobeans.mvc.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.protobeans.mvc.util.MessageConvertersBean;
import org.protobeans.mvc.util.ProtoBeansDefinitionScanner;
import org.protobeans.mvc.util.ResourcesVersionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
public class DispatcherServletContextConfig extends WebMvcConfigurerAdapter {
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
    public void scanComponents() {
        protoBeansDefinitionScanner.scan(ctx);
    }
    
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        
        validatorFactoryBean.setValidationMessageSource(messageSource);
        
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
    
    @Bean
    public StandardServletMultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }
}
