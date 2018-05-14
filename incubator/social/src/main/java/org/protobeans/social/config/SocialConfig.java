package org.protobeans.social.config;

import org.protobeans.core.config.CoreConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocialConfig {
    static {
        CoreConfig.addWebAppContextConfigClass(SocialWebConfig.class);
    }
}
