package org.cowco.lime.auth.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class User {
    @Id
    private long id;
    private String email;
    private String hashedSaltedPassword;
    private String resetPasswordToken;
    private Date resetTokenExpiry;
    private boolean isActive;
    private String loginToken;
    private Date loginExpiry;
    private Date created;
}
