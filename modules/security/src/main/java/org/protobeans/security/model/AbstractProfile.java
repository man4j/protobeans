package org.protobeans.security.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public interface AbstractProfile extends UserDetails, CredentialsContainer {
    Serializable getId();
    
    String getLogin();

    @Override
    String getPassword();

    Set<String> getRoles();

    String getConfirmUuid();
    
    default boolean isConfirmed() {
        return true;
    }
        
    @Override
    default String getUsername() {
        return getLogin();
    }
    
    @Override
    default Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    
    @Override
    default void eraseCredentials() {
        //empty
    }
    
    @Override
    default boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    default boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    default boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    default boolean isEnabled() {
        return true;
    }
}
