package org.protobeans.faces.config;

import java.util.Locale;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.omnifaces.ApplicationInitializer;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.faces.ContextParamsInitializer;
import org.protobeans.faces.annotation.EnableFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;

import com.sun.faces.config.FacesInitializer;
import com.sun.faces.config.FacesInitializer2;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Configuration
@InjectFrom(EnableFaces.class)
@ComponentScan("org.protobeans.faces.util")
public class ProtobeansFacesConfig {
    public static volatile ApplicationContext springContext;
    
    @Autowired
    private ApplicationContext ctx;
    
    @PostConstruct
    public void init() {
        springContext = ctx;
    }
    
    @Bean
    public Class<? extends ServletContainerInitializer> contextParamsInitializer() {
        return ContextParamsInitializer.class;
    }

    @Bean
    public Class<? extends FacesInitializer> facesInitializer() {
        return FacesInitializer.class;
    }
    
    @Bean
    public Class<? extends FacesInitializer2> facesInitializer2() {
        return FacesInitializer2.class;
    }
    
    @Bean
    public Class<? extends ApplicationInitializer> applicationInitializer() {
        return ApplicationInitializer.class;
    }
    
    @Bean
    public ValidatorFactory validatorFactory() {
        return Validation.byDefaultProvider()
                         .configure()
                         .messageInterpolator(new FacesMessageInterpolator(new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource()), isCached())))
                         .buildValidatorFactory();
    }
    
    @Bean
    public Validator validator(ValidatorFactory validatorFactory) {
        return validatorFactory.getValidator();
    }
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        
        messageSource.setBasenames("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(isCached() ? -1 : 0);
        messageSource.setFallbackToSystemLocale(false);
        
        return messageSource;
    }
    
    private boolean isCached() {
        boolean isCached = true;
        
        if (System.getProperty("os.name").toLowerCase().contains("windows") || "false".equals(System.getProperty("cacheMessages"))) {
            isCached = false;
        }
        
        return isCached;
    }

}

class FacesMessageInterpolator implements MessageInterpolator {
    private final MessageInterpolator delegate;
    
    public FacesMessageInterpolator(MessageInterpolator delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public String interpolate(String messageTemplate, Context context) {
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return delegate.interpolate(messageTemplate, context, locale);
    }
    
    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        return delegate.interpolate(messageTemplate, context, locale);
    }
}
