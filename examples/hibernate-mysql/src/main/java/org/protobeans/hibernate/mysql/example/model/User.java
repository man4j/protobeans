package org.protobeans.hibernate.mysql.example.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity
@DynamicUpdate
@OptimisticLocking(type = OptimisticLockType.DIRTY)
public class User {
    @Id
    private String id = UUID.randomUUID().toString();
    
    private String email;
    
    private String password;
    
    private String confirmUuid = UUID.randomUUID().toString();

    private boolean confirmed;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public User() {
        //empty
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmUuid() {
        return confirmUuid;
    }

    public void setConfirmUuid(String confirmUuid) {
        this.confirmUuid = confirmUuid;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getId() {
        return id;
    }
}
