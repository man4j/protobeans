package org.protobeans.security.model;

import org.protobeans.security.validation.SignIn;

import jakarta.validation.constraints.NotBlank;

@SignIn
public class SignInForm {
    @NotBlank(message = "{form.login.empty}")
    private String login;

    @NotBlank(message = "{form.password.empty}")
    private String password;

    private boolean rememberMe;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
