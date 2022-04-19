package org.protobeans.webapp.example.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.protobeans.postgresql.converter.HibernateString2SetConverter;
import org.protobeans.security.model.AbstractProfile;

@Entity
public class UserProfile implements AbstractProfile {
    @Id
    private String email;
    
    public UserProfile() {
        // empty
    }

    @Convert(converter = HibernateString2SetConverter.class)
    private Set<String> roles = new HashSet<>();

    private String password;
    
    private String confirmUuid;
    
    private boolean confirmed;
    
    private String userName;

    @Override
    public String getId() {
        return email;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getConfirmUuid() {
        return confirmUuid;
    }
    
    public void setConfirmUuid(String confirmUuid) {
        this.confirmUuid = confirmUuid;
    }

    @Override
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public boolean isLocked() {
        return false;
    }
}
