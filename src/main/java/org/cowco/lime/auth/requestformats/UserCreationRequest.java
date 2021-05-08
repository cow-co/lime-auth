package org.cowco.lime.auth.requestformats;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserCreationRequest {
    private static final String PASSWORD_VALIDATION_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{10,}$";
    private static final int PASSWORD_MIN_LENGTH = 13; 

    @NotEmpty
    @Email
    @Column(unique = true)
    @Size(max = 255)
    private String email;
    @NotEmpty
    @Size(max = 255)
    @Pattern(regexp = PASSWORD_VALIDATION_REGEX)
    private String password;
    @NotEmpty
    @Size(min = PASSWORD_MIN_LENGTH, max = 255)
    @Pattern(regexp = PASSWORD_VALIDATION_REGEX)
    private String passwordConfirmation;
    
    public UserCreationRequest(String email, String password, String passwordConfirmation) {
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
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

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    
}
