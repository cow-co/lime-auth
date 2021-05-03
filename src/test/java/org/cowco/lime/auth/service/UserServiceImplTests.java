package org.cowco.lime.auth.service;

import javax.persistence.EntityManager;

import org.cowco.lime.auth.model.User;
import org.cowco.lime.auth.repository.UserRepository;
import org.cowco.lime.auth.requestformats.UserCreationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class UserServiceImplTests {
    @Autowired
    private UserServiceImpl service;
    @Autowired
    private UserRepository repository;
    @Autowired
    private EntityManager entityManager;

    @Test
    public void testCreatesValidUser() {
        UserCreationRequest userCreationRequest = new UserCreationRequest("email@email.com", "SomeSuperDuperL0ngPassword!", "SomeSuperDuperL0ngPassword");
        User found = repository.findByEmail(userCreationRequest.getEmail());
        assertThat(found).isNull();

        service.addUser(userCreationRequest);
        found = repository.findByEmail(userCreationRequest.getEmail());
        assertThat(found.getEmail()).isEqualTo(userCreationRequest.getEmail());
    }
}
