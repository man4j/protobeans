package org.protobeans.security.model;

import org.springframework.security.authentication.AuthenticationProvider;

/**
 * Можно и без враппера, но если помещать бины провайдеров в контекст напрямую, то включается разного рода автоконфигурация
 */
public interface AuthenticationProviderWrapper {
    
    AuthenticationProvider getProvider();

}
