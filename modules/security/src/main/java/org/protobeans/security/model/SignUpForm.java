package org.protobeans.security.model;

import org.protobeans.security.validation.FieldEquality;
import org.protobeans.security.validation.LoginNotExists;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@FieldEquality(field1 = "password", field2 = "confirmPassword", message = "{form.password.notEquals}")
public class SignUpForm {
    @NotBlank(message = "{form.login.empty}")
    @LoginNotExists
    private String login;

    @NotNull(message = "{form.password.empty}")
    @Size(min = 6, max = 18, message = "{form.password.size}")
    @Pattern(regexp = "[a-zA-Z0-9_-]*", message = "{form.password.format}")
    private String password;

    @NotNull(message = "{form.confirmPassword.empty}")
    private String confirmPassword;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
