package org.protobeans.security.model;

import java.util.Locale;

import javax.validation.constraints.NotBlank;

import org.protobeans.security.validation.SignIn;

@SignIn
public class SignInForm {
    @NotBlank(message = "{form.id.empty}")
    private String id;

    @NotBlank(message = "{form.password.empty}")
    private String password;

    private boolean rememberMe;

    public String getId() {
        return id == null ? null : id.toLowerCase(Locale.US);
    }

    public void setId(String id) {
        this.id = id;
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
