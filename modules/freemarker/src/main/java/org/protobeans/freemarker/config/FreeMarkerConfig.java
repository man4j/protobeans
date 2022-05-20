package org.protobeans.freemarker.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.freemarker.annotation.EnableFreeMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateExceptionHandler;

@Configuration
@InjectFrom(EnableFreeMarker.class)
public class FreeMarkerConfig {
    private String devMode;
    
    @Autowired(required = false)
    private List<TemplateLoader> templateLoaders = new ArrayList<>();
    
    @Bean
    public freemarker.template.Configuration freeMarkerConfiguration() {
        freemarker.template.Configuration config = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
        
        DefaultObjectWrapper wrapper = (DefaultObjectWrapper) freemarker.template.Configuration.getDefaultObjectWrapper(freemarker.template.Configuration.VERSION_2_3_31);
        
        templateLoaders.add(new SpringTemplateLoader(new DefaultResourceLoader(), "classpath:/templates"));
        
        MultiTemplateLoader tl = new MultiTemplateLoader(templateLoaders.toArray(new TemplateLoader[] {})); 
        
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
        config.addAutoImport("f", "/lib/forms.ftlh");
        config.addAutoImport("u", "/lib/util.ftlh");
        config.addAutoImport("s", "/lib/security.ftlh");
        
        return config;
    }
    
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
