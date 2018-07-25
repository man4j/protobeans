package org.protobeans.mvc.config;

import java.lang.reflect.Field;

import org.protobeans.mvc.util.ProtobeansMessageInterpolator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Вынесен в отдельный конфиг, т.к. здесь инициализируется постпроцессор и это мешает основному постпроцессору протобинс
 * инъектить параметры 
 */
@Configuration
public class MvcValidatorConfig {
    @Autowired
    private MessageSource messageSource;

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
          
        methodValidationPostProcessor.setValidator(localValidatorFactoryBean());
           
        return methodValidationPostProcessor;
    }
    
    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
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
}
