package org.protobeans.mvc.config;

import org.protobeans.mvc.util.ProtobeansMessageInterpolator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Вынесен в отдельный конфиг, т.к. здесь инициализируется постпроцессор и это мешает основному постпроцессору протобинс
 * инъектить параметры 
 */
@Configuration
public class MvcValidatorConfig {
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
          
        methodValidationPostProcessor.setValidator(localValidatorFactoryBean());
           
        return methodValidationPostProcessor;
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
    
    @Bean
    @Primary
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        
        validatorFactoryBean.setMessageInterpolator(new ProtobeansMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource()), isCached()));
        
        return validatorFactoryBean;
    }
}
