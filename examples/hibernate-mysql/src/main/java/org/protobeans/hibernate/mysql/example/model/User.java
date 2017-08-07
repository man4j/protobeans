package org.protobeans.hibernate.mysql.example.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity
@Table(name = "users")
@DynamicUpdate
@OptimisticLocking(type = OptimisticLockType.DIRTY)
public class User {
    @Id
    private int id;
    
    private String email;
    
    private String password;
    
    @Column(name = "confirm_uuid")
    private String confirmUuid;

    private boolean confirmed;

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.confirmUuid = UUID.randomUUID().toString();
    }
    
    public User(int id, String email, String password, String confirmUuid) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.confirmUuid = confirmUuid;
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

    public int getId() {
        return id;
    }
}
