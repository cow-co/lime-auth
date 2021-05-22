package org.cowco.lime.auth.repository;

import javax.persistence.EntityManager;

import org.cowco.lime.auth.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.*;

import java.util.NoSuchElementException;

@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository repository;
    @Autowired
    private EntityManager entityManager;


    @Test
    public void testFindsUserByEmail() {
        User user = new User("email@email.com", "pass");
        entityManager.persist(user);
        entityManager.flush();

        User found = repository.findByEmail(user.getEmail()).orElseThrow();
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testFailsToFindNonExistentUserByEmail() {
        User user = new User("email@email.com", "pass");
        entityManager.persist(user);
        entityManager.flush();
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> repository.findByEmail("user.getEmail()").orElseThrow());
    }

    @Test
    public void testDeletesUser() {
        User user = new User("email@email.com", "pass");
        entityManager.persist(user);
        entityManager.flush();

        User found = repository.findByEmail(user.getEmail()).orElseThrow();
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
        repository.delete(found);
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> repository.findByEmail(user.getEmail()).orElseThrow());
    }

    @Test
    public void testSavesUser() {
        User user = new User("email@email.com", "pass");        
        repository.save(user);
        User found = repository.findByEmail(user.getEmail()).orElseThrow();
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
    }
}
