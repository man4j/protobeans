package org.protobeans.security.model;

import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UuidAuthenticationToken extends AbstractAuthenticationToken {
    private String uuid;
    
    public UuidAuthenticationToken(String email, String uuid) {
        super((Collection<? extends GrantedAuthority>) null);
        setDetails(email);
        this.uuid = uuid;
        super.setAuthenticated(true);
    }
    
    public UuidAuthenticationToken(AbstractProfile principal, @Nullable String uuid, Collection<SimpleGrantedAuthority> authorities) {
        super(authorities);
        setDetails(principal);
        this.uuid = uuid;
        super.setAuthenticated(true); // must use super, as we override
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
