package org.protobeans.freemarker.config;

import java.nio.charset.StandardCharsets;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.core.config.CoreConfig;
import org.protobeans.freemarker.annotation.EnableFreeMarker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateExceptionHandler;

@Configuration
@InjectFrom(EnableFreeMarker.class)
public class FreeMarkerConfig {
    static {
        CoreConfig.addWebAppContextConfigClass(FreeMarkerWebConfig.class);
    }
    
    private String devMode;
    
    @Bean
    public freemarker.template.Configuration freeMarkerConfiguration() {
        freemarker.template.Configuration config = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_24);
        
        DefaultObjectWrapper wrapper = (DefaultObjectWrapper) freemarker.template.Configuration.getDefaultObjectWrapper(freemarker.template.Configuration.VERSION_2_3_24);
        
        MultiTemplateLoader tl = new MultiTemplateLoader(new TemplateLoader[] {new SpringTemplateLoader(new DefaultResourceLoader(), "classpath:/templates"), 
                                                                               new SpringTemplateLoader(new DefaultResourceLoader(), "classpath:/org/springframework/web/servlet/view/freemarker")});
        
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
}
