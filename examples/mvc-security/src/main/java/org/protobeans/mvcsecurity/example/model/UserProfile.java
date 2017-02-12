package org.protobeans.mvcsecurity.example.model;

import org.protobeans.security.model.AbstractProfile;

public class UserProfile extends AbstractProfile {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
