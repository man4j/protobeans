package org.protobeans.freemarker.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.freemarker.annotation.EnableFreemarker;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateExceptionHandler;

@Configuration
@InjectFrom(EnableFreemarker.class)
public class FreemarkerConfig {
    private String devMode;
    
    @Bean
    public freemarker.template.Configuration freeMarkerConfiguration() {
        freemarker.template.Configuration config = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_24);
        
        DefaultObjectWrapper wrapper = (DefaultObjectWrapper) freemarker.template.Configuration.getDefaultObjectWrapper(freemarker.template.Configuration.VERSION_2_3_24);
        
        TemplateLoader tl = new ClassTemplateLoader(FreemarkerConfig.class, "/templates"); 
        
        config.setAutoFlush(false);
        config.setLogTemplateExceptions(false);
        
        config.setDefaultEncoding(StandardCharsets.UTF_8.name());
        config.setOutputEncoding(StandardCharsets.UTF_8.name());
        
        config.setTemplateUpdateDelayMilliseconds("true".equals(devMode) ? 0 : Integer.MAX_VALUE);
        config.setTemplateLoader(tl);
        config.setTemplateExceptionHandler("true".equals(devMode) ? TemplateExceptionHandler.HTML_DEBUG_HANDLER : TemplateExceptionHandler.RETHROW_HANDLER);

        config.setSharedVariable("enums", wrapper.getEnumModels());//access from template: ${enums["java.math.RoundingMode"].UP}
        config.setSharedVariable("statics",  wrapper.getStaticModels());//access from template: ${statics["java.lang.System"].currentTimeMillis()}
        
        config.addAutoImport("ui", "/lib/composition.ftlh");
        
        return config;
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
