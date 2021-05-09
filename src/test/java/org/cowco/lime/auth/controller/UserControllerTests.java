package org.cowco.lime.auth.controller;

import javax.persistence.EntityManager;

import org.cowco.lime.auth.controller.UserController;
import org.cowco.lime.auth.model.User;
import org.cowco.lime.auth.repository.UserRepository;
import org.cowco.lime.auth.requestformats.UserCreationRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(UserController.class)
@TestPropertySource(value = "classpath:application-test.properties")
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserRepository repository;

    @Test
    public void testCreatesValidUser() throws Exception {
        given(repository.save(any())).willReturn(new User("email@email.com", "hash"));
        UserCreationRequest userCreationRequest = new UserCreationRequest("email@email.com", "SomeSuperDuperL0ngPassword!", "SomeSuperDuperL0ngPassword!");
        mvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(userCreationRequest))).andExpect(status().isOk());
    }

    @Test
    public void testDoesNotCreateInvalidEmailUser() throws Exception {
        UserCreationRequest userCreationRequest = new UserCreationRequest("email.com", "SomeSuperDuperL0ngPassword!", "SomeSuperDuperL0ngPassword!");
        mvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(userCreationRequest))).andExpect(status().isBadRequest());
    }

    @Test
    public void testDoesNotCreateEmptyEmailUser() throws Exception {
        UserCreationRequest userCreationRequest = new UserCreationRequest("", "SomeSuperDuperL0ngPassword!", "SomeSuperDuperL0ngPassword!");
        mvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(userCreationRequest))).andExpect(status().isBadRequest());
    }

    @Test
    public void testDoesNotCreateNullEmailUser() throws Exception {
        UserCreationRequest userCreationRequest = new UserCreationRequest(null, "SomeSuperDuperL0ngPassword!", "SomeSuperDuperL0ngPassword!");
        mvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(userCreationRequest))).andExpect(status().isBadRequest());
    }

    @Test
    public void testDoesNotCreateShortPasswordUser() throws Exception {
        UserCreationRequest userCreationRequest = new UserCreationRequest("email@email.com", "Sh0!rt", "Sh0!rt");
        mvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(userCreationRequest))).andExpect(status().isBadRequest());
    }

    @Test
    public void testDoesNotCreateUserWithNoNumbersInPassword() throws Exception {
        UserCreationRequest userCreationRequest = new UserCreationRequest("email@email.com", "SomeSuperDuperLongPassword!", "SomeSuperDuperLongPassword!");
        mvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(userCreationRequest))).andExpect(status().isBadRequest());
    }

    @Test
    public void testDoesNotCreateUserWithNoUpperCaseInPassword() throws Exception {
        UserCreationRequest userCreationRequest = new UserCreationRequest("email@email.com", "somesuperduperl0ngpassword!", "somesuperduperl0ngpassword!");
        mvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(userCreationRequest))).andExpect(status().isBadRequest());
    }

    @Test
    public void testDoesNotCreateUserWithNoLowerCaseInPassword() throws Exception {
        UserCreationRequest userCreationRequest = new UserCreationRequest("email@email.com", "SOMESUPERDUPERL0NGPASSWORD!", "SOMESUPERDUPERL0NGPASSWORD!");
        mvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(userCreationRequest))).andExpect(status().isBadRequest());
    }

    @Test
    public void testDoesNotCreateUserWithNoSymbolsInPassword() throws Exception {
        UserCreationRequest userCreationRequest = new UserCreationRequest("email@email.com", "SomeSuperDuperL0ngPassword", "SomeSuperDuperL0ngPassword");
        mvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(userCreationRequest))).andExpect(status().isBadRequest());
    }

    @Test
    public void testDoesNotCreateUserWithNonMatchingConfirmation() throws Exception {
        UserCreationRequest userCreationRequest = new UserCreationRequest("email@email.com", "SomeSuperDuperL0ngPassword!", "SomeSuperDuperL1ngPassword!");
        mvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(userCreationRequest))).andExpect(status().isBadRequest());
    }
}
