package com.example.user.services;

import com.example.user.domain.User;
import com.example.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class provides some services. Its utility is to make business logic
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * This method displays the details of a registered user
     *
     * @param id long
     * @return Optional<User>
     */
    public Optional<User> getUser(long id) {
        return userRepository.findById(id);
    }

    /**
     * This method allows to register a user in database
     *
     * @param userData User
     * @return User
     */
    public User registerNewUser(User userData) {
        return Optional.ofNullable(userRepository.save(userData))
                .orElseThrow(() -> new DataIntegrityViolationException("Couldn't create a new user"));
    }
}
