package org.protobeans.security.model;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UuidAuthenticationToken extends AbstractAuthenticationToken {
    private String uuid;
    
    public UuidAuthenticationToken(Object principal, String uuid, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        setDetails(principal);
        this.uuid = uuid;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return uuid;
    }

    @Override
    public Object getPrincipal() {
        return getDetails();
    }
}
