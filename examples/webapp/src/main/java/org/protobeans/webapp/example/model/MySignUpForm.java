package org.protobeans.webapp.example.model;

import org.protobeans.security.model.SignUpForm;

import lombok.Getter;
import lombok.Setter;

public class MySignUpForm extends SignUpForm {
    @Getter
    @Setter
    private String userName = "";
}
