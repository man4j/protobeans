package org.protobeans.i18n.config;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.i18n.annotation.EnableI18n;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@InjectFrom(EnableI18n.class)
public class I18nConfig {
    private String isCached;
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasenames("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds("true".equals(isCached) ? -1 : 0);
        messageSource.setFallbackToSystemLocale(false);

        return messageSource;
    }
}
