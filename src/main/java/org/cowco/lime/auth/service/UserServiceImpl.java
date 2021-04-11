package org.cowco.lime.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cowco.lime.auth.exceptions.ValidationException;
import org.cowco.lime.auth.model.User;
import org.cowco.lime.auth.repository.UserRepository;
import org.cowco.lime.auth.requestformats.UserCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final String PASSWORD_VALIDATION_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^0-9A-Za-z])$";
    private static final int PASSWORD_MIN_LENGTH = 13; 
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

    public UserServiceImpl() {
        calculateArgon2Settings();
    }

    private void calculateArgon2Settings() {
        long currentIterationsTime = 0;
        int currentIterations = MIN_ITERATIONS;

        while(currentIterationsTime < MIN_HASH_TIME_MILLIS) {
            currentIterations++;
            passwordEncoder = new Argon2PasswordEncoder(ARGON2_SALT_LENGTH_BYTES, ARGON2_HASH_LENGTH_BYTES, ARGON2_THREADS, ARGON2_MEM, currentIterations);
            long startTime = System.nanoTime();
            passwordEncoder.encode(ITERATIONS_TEST_STRING);
            long endTime = System.nanoTime();
            long hashTime = endTime - startTime;
            currentIterationsTime = hashTime / 1000000;    // Convert nanoseconds to milliseconds
        }
    }

    @Override
    public void addUser(UserCreationRequest userCreationRequest) {
        List<String> validationErrors = verifyUserCreationRequest(userCreationRequest);

        if(validationErrors.size() > 0) {
            // TODO Return the errors back out to the controller
        }

        // NOTE The backend for this does not parallelise as well as hash-cracking systems do. So there is attacker/defender asymmetry there.
        String hashedPassword = passwordEncoder.encode(userCreationRequest.getPassword());
        
        // TODO Create the user

        userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    @Override
    public void deleteUserById(long id) {
        userRepository.delete(id);
    }

    private List<String> verifyUserCreationRequest(UserCreationRequest request) throws ValidationException {
        List<String> errors = new ArrayList<>();
        String password = request.getPassword();
        Pattern passwordValidationPattern = Pattern.compile(PASSWORD_VALIDATION_REGEX);
        Matcher passwordMatcher = passwordValidationPattern.matcher(password);
        
        if(password == null || password.isEmpty()) {
            errors.add("Password must be populated");
        }

        if(!passwordMatcher.matches()) {
            errors.add("Password must include at least one special character, one number, one upper-case and one lower-case character.");
        }

        if(password.length() < PASSWORD_MIN_LENGTH) {
            errors.add("Password must be at least" + PASSWORD_MIN_LENGTH + " characters long");
        }
        
        if(!password.equals(request.getPasswordConfirmation())) {
            errors.add("Password confirmation must match");
        }

        return errors;
    }
}
