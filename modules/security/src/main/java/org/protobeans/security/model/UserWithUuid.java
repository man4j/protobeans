package org.protobeans.security.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserWithUuid extends User {
    private String uuid;
    
    public UserWithUuid(String username, String password, boolean enabled, String uuid, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
        this.uuid = uuid;
    }
    
    public String getUuid() {
        return uuid;
    }
}
