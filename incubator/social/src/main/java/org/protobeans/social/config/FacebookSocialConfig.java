package org.protobeans.social.config;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.social.annotation.EnableFacebook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@Configuration
@InjectFrom(EnableFacebook.class)
public class FacebookSocialConfig {
    private String appId;
    
    private String appSecret;
    
    @Bean
    public ConnectionFactory<Facebook> facebookConnectionFactory() {
        return new FacebookConnectionFactory(appId, appSecret);
    }
}
