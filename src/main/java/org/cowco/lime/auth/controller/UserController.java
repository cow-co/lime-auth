package org.cowco.lime.auth.controller;

import javax.validation.Valid;

import org.cowco.lime.auth.model.User;
import org.cowco.lime.auth.repository.UserRepository;
import org.cowco.lime.auth.requestformats.UserCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final int ARGON2_HASH_LENGTH_BYTES = 32; // 256 bits
    private static final int ARGON2_SALT_LENGTH_BYTES = 16; // 128 bits
    private static final int MIN_HASH_TIME_MILLIS = 1000;   // We want the hashing to take at least one second on our system, to mitigate brute-force attacks
    private static final int ARGON2_THREADS = 2;
	private static final int ARGON2_MEM = 1 << 12;          // This is the Spring Security default value
	private static final int MIN_ITERATIONS = 10;
    private static final String ITERATIONS_TEST_STRING = "SomeTestString";

    @Autowired
    private UserRepository userRepository;
    private Argon2PasswordEncoder passwordEncoder;
    @Value("${ARGON_PEPPER}")
    private String pepper;
    @Value("${argon.adaptive.enabled:true}")
    private boolean isAdaptiveArgonSettingsEnabled;

    public UserController() {
        calculateArgon2Settings();
    }

    private void calculateArgon2Settings() {
        long currentIterationsTime = 0;
        int currentIterations = MIN_ITERATIONS;

        if(isAdaptiveArgonSettingsEnabled) {
            while(currentIterationsTime < MIN_HASH_TIME_MILLIS) {
                currentIterations++;
                passwordEncoder = new Argon2PasswordEncoder(ARGON2_SALT_LENGTH_BYTES, ARGON2_HASH_LENGTH_BYTES, ARGON2_THREADS, ARGON2_MEM, currentIterations);
                long startTime = System.nanoTime();
                passwordEncoder.encode(ITERATIONS_TEST_STRING);
                long endTime = System.nanoTime();
                long hashTime = endTime - startTime;
                currentIterationsTime = hashTime / 1000000;    // Convert nanoseconds to milliseconds
            }
        } else {
            passwordEncoder = new Argon2PasswordEncoder(ARGON2_SALT_LENGTH_BYTES, ARGON2_HASH_LENGTH_BYTES, ARGON2_THREADS, ARGON2_MEM, currentIterations);
        }
    }

    @PutMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserCreationRequest userCreationRequest) {
        ResponseEntity<String> response;

        if(userCreationRequest.getPassword().equals(userCreationRequest.getPasswordConfirmation())) {
            String passwordAndPepper = userCreationRequest.getPassword().concat(pepper);
            String hashedPassword = passwordEncoder.encode(passwordAndPepper);  // NOTE The backend for this does not parallelise as well as hash-cracking systems do. 
                                                                                // So there is attacker/defender asymmetry there.
            User user = new User(userCreationRequest.getEmail(), hashedPassword);
            userRepository.save(user);
            response = ResponseEntity.ok("User Successfully Created");
        } else {
            response = ResponseEntity.badRequest().body("Password confirmation must match");
        }

        return response;
    }
}
