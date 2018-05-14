package org.protobeans.mvcsecurity.example.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.protobeans.hibernate.converter.HibernateString2SetConverter;
import org.protobeans.security.model.AbstractProfile;

@Entity
@Table(name="users")
public class UserProfile implements AbstractProfile {
    @Id
    private String id;
    
    public UserProfile(String id) {
        this.id = id;
    }
    
    public UserProfile() {
        // empty
    }

    @Convert(converter = HibernateString2SetConverter.class)
    private Set<String> roles = new HashSet<>();
    
    private String email;
    
    private String password;
    
    @Column(name = "confirm_uuid")
    private String confirmUuid;
    
    private boolean confirmed;
    
    private String userName;

    public String getId() {
        return id;
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

    @Override
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
}
