package org.protobeans.social.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.protobeans.social.config.FacebookSocialConfig;
import org.protobeans.social.config.SocialConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({SocialConfig.class, FacebookSocialConfig.class})
@Configuration
public @interface EnableFacebook {
    String appId();
    String appSecret();
}
