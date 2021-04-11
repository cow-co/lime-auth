package org.cowco.lime.auth.repository;

import javax.persistence.EntityManager;

import org.cowco.lime.auth.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository repository;
    @Autowired
    private EntityManager entityManager;


    @Test
    public void testFindsUserByEmail() {
        User user = new User("email", "pass", "salty");
        entityManager.persist(user);
        entityManager.flush();

        User found = repository.findByEmail(user.getEmail());
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testFailsToFindNonExistentUserByEmail() {
        User user = new User("email", "pass", "salty");
        entityManager.persist(user);
        entityManager.flush();

        User found = repository.findByEmail("user.getEmail()");
        assertThat(found).isNull();
    }

    @Test
    public void testDeletesUser() {
        User user = new User("email", "pass", "salty");
        entityManager.persist(user);
        entityManager.flush();

        User found = repository.findByEmail(user.getEmail());
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
        repository.delete(found);
        found = repository.findByEmail(user.getEmail());
        assertThat(found).isNull();
    }

    @Test
    public void testSavesUser() {
        User user = new User("email", "pass", "salty");
        User found = repository.findByEmail(user.getEmail());
        assertThat(found).isNull();
        
        repository.save(user);
        found = repository.findByEmail(user.getEmail());
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
    }
}
