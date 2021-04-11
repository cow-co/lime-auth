package org.cowco.lime.auth.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // Uses a simple email address validation regex
    @NotBlank(message = "Email is a mandatory field")
    @Pattern(regexp = "^(.+)@(.+)$")
    private String email;
    @NotBlank(message = "Password is a required field")
    private String hashedPassword;
    private String salt;    // There is also a pepper value, stored in env vars and read in to the repository class. 
                            // This means an attacker will require access to both the DB and the app container (or the CD platform)
    private String registerToken;
    private String loginToken;
    private boolean isActive;   // Default this to false; set to true once registration is confirmed. Can be set back to false if the user is banned
    private String resetToken;
    private String twoFAToken;
    private String oAuthToken;


    public User(String email, String hashedPassword, String salt) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
