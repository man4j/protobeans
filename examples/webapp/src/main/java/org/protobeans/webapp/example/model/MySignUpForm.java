package org.protobeans.webapp.example.model;

import org.protobeans.security.model.SignUpForm;

public class MySignUpForm extends SignUpForm {
    private String userName = "";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
