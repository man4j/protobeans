package org.protobeans.security.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.protobeans.security.model.AbstractProfile;
import org.protobeans.security.model.UserWithUuid;
import org.protobeans.security.model.UuidAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {        
            Object principal = auth.getPrincipal();
    
            if (principal instanceof User) {
                return (User) principal;
            }
        }

        return null;
    }
    
    public Authentication createUsernamePasswordAuthenticationToken(AbstractProfile profile, String rawPassword) {
        List<GrantedAuthority> authorities = profile.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(new User(profile.getId(), profile.getPassword(), profile.isConfirmed(), true, true, true, authorities), rawPassword, authorities);
    }
    
    public Authentication createUuidAuthenticationToken(AbstractProfile profile, String uuid) {
        List<GrantedAuthority> authorities = profile.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new UuidAuthenticationToken(new UserWithUuid(profile.getId(), profile.getPassword(), profile.isConfirmed(), profile.getConfirmUuid(), authorities), uuid, authorities);
    }

    public boolean isCurrentUser(String login) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth.getName().equals(login);
    }

    public String getCurrentPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth.getName();
    }

    public List<String> getRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    public String generatePassword() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }
}