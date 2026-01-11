package org.protobeans.security.model;

import org.protobeans.security.validation.SignIn;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SignIn
public class SignInForm {
    @NotBlank(message = "{form.login.empty}")
    private String login;

    @NotBlank(message = "{form.password.empty}")
    private String password;

    private boolean rememberMe;
}
