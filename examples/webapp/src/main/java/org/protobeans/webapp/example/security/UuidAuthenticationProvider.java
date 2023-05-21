package org.protobeans.webapp.example.security;

import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.model.UuidAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class UuidAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UuidAuthenticationToken token = (UuidAuthenticationToken) authentication;
        
        String requestedUuid = (String) token.getCredentials();
        String savedUuid = ((AbstractProfile) token.getPrincipal()).getConfirmUuid();
        
        if (!savedUuid.equals(requestedUuid)) {
            throw new BadCredentialsException("Uuids not equals");
        }
        
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UuidAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
