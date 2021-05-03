package org.cowco.lime.auth.service;

import java.util.List;

import org.cowco.lime.auth.model.User;
import org.cowco.lime.auth.requestformats.UserCreationRequest;

// Implementations of this interface will define the business logic
public interface UserService {
    List<String> addUser(UserCreationRequest user);
    User getUserById(long id);
    User getUserByEmail(String email);
    void deleteUserById(long id);
}
