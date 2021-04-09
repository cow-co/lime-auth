package org.cowco.lime.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.cowco.lime.auth.model.User;

// This is the direct interface to the database
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
