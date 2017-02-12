package org.protobeans.freemarker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class FreeMarkerWebConfig {
    /**
     * Needs for FreeMarkerView
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(freemarker.template.Configuration cfg) {
        FreeMarkerConfigurer fmc = new FreeMarkerConfigurer();
        
        fmc.setConfiguration(cfg);
        
        return fmc;
    }
    
    @Bean
    @Lazy
    public ViewResolver viewResolver() {
        FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
        
        viewResolver.setSuffix(".ftlh");
        viewResolver.setContentType("text/html;charset=UTF-8");
        viewResolver.setRequestContextAttribute("rc");
        viewResolver.setExposeContextBeansAsAttributes(true);
        viewResolver.setExposeRequestAttributes(true);
        viewResolver.setExposeSessionAttributes(true);
        
        return viewResolver;
    }
}
