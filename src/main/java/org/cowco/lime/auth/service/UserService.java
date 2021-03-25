package org.cowco.lime.auth.service;

import org.cowco.lime.auth.model.User;

// Implementations of this interface will define the business logic
public interface UserService {
    void addUser(User user);
    User getUserById(long id);
    User getUserByEmail(String email);
    void deleteUserById(long id);
}
