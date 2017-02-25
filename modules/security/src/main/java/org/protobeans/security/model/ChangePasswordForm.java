package org.protobeans.security.model;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.protobeans.security.validation.CurrentPassword;
import org.protobeans.security.validation.FieldEquality;

@FieldEquality(field1 = "password", field2 = "confirmPassword", message = "{change.passwordsNotEquals}")
public class ChangePasswordForm {
    @NotBlank(message = "{form.email.empty}")
    @CurrentPassword
    private String oldPassword = "";

    @Size(min = 6, max = 18, message = "{form.password.size}")
    @Pattern(regexp = "[a-zA-Z0-9_-]*", message = "{form.password.format}")
    private String password = "";

    @Size(min = 6, max = 18, message = "{form.confirmPassword.size}")
    @Pattern(regexp = "[a-zA-Z0-9_-]*", message = "{form.confirmPassword.format}")
    private String confirmPassword = "";

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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
