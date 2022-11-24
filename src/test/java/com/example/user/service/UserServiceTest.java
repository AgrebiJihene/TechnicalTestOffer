package com.example.user.service;

import com.example.user.domain.Gender;
import com.example.user.domain.User;
import com.example.user.repositories.UserRepository;
import com.example.user.services.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit Tests for the service
 */
@RunWith(SpringRunner.class)
public class UserServiceTest {
    private UserService userService;

    private UserRepository userRepository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private User user;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
        String birthdateValue = "1964-12-11";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth = LocalDate.parse(birthdateValue, formatter);
        user = new User(1L, "jihane", birth, "France", "328172", Gender.FEMALE);
    }

    /**
     * This test checks if the service registers a user that not already exists
     */
    @Test
    public void save_ShouldSuccessWhenUserNotAlreadyExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        User response = userService.registerNewUser(user);
        assertEquals(response.getId(), 1L);
    }

    /**
     * This test checks if the service refuses to add a user that already exists
     */
    @Test
    public void save_shouldThrowExceptionIfUserExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        expectedException.expect(DataIntegrityViolationException.class);
        userService.registerNewUser(user);
        expectedException.expectMessage("User already exists");
    }

    /**
     * This test checks if the service displays the details of a user via the id
     */
    @Test
    public void shouldReturnUser() {
        long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        Optional<User> response = userService.getUser(id);
        assertEquals(response.get().getId(), id);
    }

}
