package org.cowco.lime.auth.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserTests {
    @Test
    public void testSuccessfullyValidatesUser() {
        try {
            User user = new User("email@email.com", "pass");
        } catch(ConstraintViolationException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testIncorrectEmailFailsValidation() {
        ConstraintViolationException thrown = assertThrows(
            ConstraintViolationException.class, 
            () -> {
                User user = new User("email", "pass");
            }
        );
    }
}
