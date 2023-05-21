package org.protobeans.webapp.example.entity;

import java.util.HashSet;
import java.util.Set;

import org.protobeans.postgresql.converter.HibernateString2SetConverter;
import org.protobeans.security.model.AbstractProfile;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class UserProfile implements AbstractProfile {
    @Id
    @Getter
    @Setter
    private String email;
    
    public UserProfile() {
        // empty
    }

    @Getter
    @Convert(converter = HibernateString2SetConverter.class)
    private Set<String> roles = new HashSet<>();

    @Getter
    @Setter
    private String password;
    
    @Getter
    @Setter
    private String confirmUuid;
    
    @Getter
    @Setter
    private boolean confirmed;
    
    @Getter
    @Setter
    private String userName;

    @Override
    public String getLogin() {
        return email;
    }

    @Override
    public String getId() {
        return email;
    }
}
