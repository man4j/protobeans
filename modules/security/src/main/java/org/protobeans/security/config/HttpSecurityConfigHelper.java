package org.protobeans.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@FunctionalInterface
public interface HttpSecurityConfigHelper {
    void configure(HttpSecurity http) throws Exception;
}
