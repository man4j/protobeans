package org.protobeans.mvc.config;

import static org.springdoc.core.Constants.CLASSPATH_RESOURCE_LOCATION;
import static org.springdoc.core.Constants.DEFAULT_WEB_JARS_PREFIX_URL;
import static org.springdoc.core.Constants.SWAGGER_UI_PATH;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.ui.SwaggerIndexTransformer;
import org.springdoc.ui.SwaggerWelcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@SuppressWarnings("deprecation")
public class Swagger3Config extends WebMvcConfigurerAdapter { // NOSONAR
    static {
        System.setProperty("springdoc.webjars.prefix", "/v3/webjars");
        System.setProperty("springdoc.swagger-ui.path", "/v3/swagger-ui.html");
    }
    
    @Value(SWAGGER_UI_PATH)
    private String swaggerPath;
    
    @Autowired
    private SwaggerUiOAuthProperties swaggerUiOAuthProperties;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private SwaggerUiConfigProperties swaggerUiConfigProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        StringBuilder uiRootPath = new StringBuilder();
        if (swaggerPath.contains("/"))
            uiRootPath.append(swaggerPath, 0, swaggerPath.lastIndexOf('/'));
        uiRootPath.append("/**");
        registry.addResourceHandler(uiRootPath + "/swagger-ui/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATION + DEFAULT_WEB_JARS_PREFIX_URL + DEFAULT_PATH_SEPARATOR)
                .resourceChain(false)
                .addTransformer(swaggerIndexTransformer(swaggerUiOAuthProperties, objectMapper));
    }

    @Bean
    public SwaggerWelcome swaggerWelcome(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties) {
        swaggerUiConfigProperties.setPath(swaggerPath);

        return new SwaggerWelcome(swaggerUiConfig, springDocConfigProperties);
    }

    @Bean
    public SwaggerIndexTransformer swaggerIndexTransformer(SwaggerUiOAuthProperties swaggerUiOAuthProperties, ObjectMapper objectMapper) {
        return new SwaggerIndexTransformer(swaggerUiOAuthProperties, objectMapper);
    }
}
