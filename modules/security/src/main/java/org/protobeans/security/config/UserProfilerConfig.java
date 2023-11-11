package org.protobeans.security.config;

import org.protobeans.core.annotation.InjectFrom;
import org.protobeans.security.annotation.EnableUsersProfiler;
import org.protobeans.security.interceptor.UsersStatsInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@InjectFrom(EnableUsersProfiler.class)
public class UserProfilerConfig {
    @Bean
    public UsersStatsInterceptor usersStatsInterceptor() {
        return new UsersStatsInterceptor();
    }
}
