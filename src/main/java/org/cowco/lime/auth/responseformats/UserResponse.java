package org.cowco.lime.auth.responseformats;

import org.cowco.lime.auth.model.User;

public class UserResponse {
    private long id;
    private String email;
    private boolean isActive;

    public UserResponse(long id, String email, boolean isActive) {
        this.id = id;
        this.email = email;
        this.isActive = isActive;
    }

    public UserResponse(User original) {
        this.id = original.getId();
        this.email = original.getEmail();
        this.isActive = original.isActive();
    }

    public long getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public boolean isActive() {
        return isActive;
    }
}
