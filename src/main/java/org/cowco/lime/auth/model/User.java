package org.cowco.lime.auth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // Uses a simple email address validation regex
    @NotEmpty
    @Email
    @Column(unique = true)
    @Size(max = 255)
    private String email;
    @NotBlank(message = "Password is a required field")
    private String hashedPassword;
    private String registerToken;
    private String loginToken;
    private boolean isActive;   // Default this to false; set to true once registration is confirmed. Can be set back to false if the user is banned
    private String resetToken;
    private String twoFAToken;
    private String oAuthToken;


    public User(@Valid String email, @Valid String hashedPassword) {
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return email;
    }
}
