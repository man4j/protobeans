package org.protobeans.mvc.config;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.protobeans.mvc.util.MessageConvertersBean;
import org.protobeans.mvc.util.ResourcesVersionBean;

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
    private AnnotationConfigWebApplicationContext ctx;
    
    @PostConstruct
    public void scanComponents() {
        if (MvcInitializer.basePackageClasses.length > 0) {
            ClassPathBeanDefinitionScanner beanDefinitionScanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry) ctx.getBeanFactory());

            beanDefinitionScanner.scan(Stream.of(MvcInitializer.basePackageClasses).map(c -> c.getPackage().getName()).collect(Collectors.toList()).toArray(new String[] {}));
        }
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
