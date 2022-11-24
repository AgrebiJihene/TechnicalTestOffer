package com.example.user.controllers;

import com.example.user.domain.User;
import com.example.user.dto.UserDto;
import com.example.user.mapper.UserMapper;
import com.example.user.services.UserService;
import com.example.user.exception.ContentNotAllowedException;
import com.example.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * A controller in charge of user request handling
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * This method takes the id of the requested user as a parameter, gets the registered user, converts it to UserDto and displays his details
     *
     * @param id long
     * @return ResponseEntity<UserDto>
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        log.info("get User dont l id est " + id);
        Optional<User> user = userService.getUser(id);

        if (!user.isPresent()) {
            log.error("ERROR, 404 NOT FOUND !");
            throw new UserNotFoundException(id);
        }
        log.info("convert user to userDto");
        UserDto userResponse = UserMapper.INSTANCE.convertToDto(user.get());

        return ResponseEntity.ok().body(userResponse);

    }

    /**
     * This method takes the date of
     * This method takes as parameters the details of the UserDto sent in the request, converts it to a User to save it in the database and returns the details of the UserDto.
     *
     * @param userDto UserDto
     * @param errors  BindingResult
     * @return ResponseEntity<UserDto>
     */
    @PostMapping(value = "/user")
    public ResponseEntity<UserDto> registerNewUser(@Valid @RequestBody UserDto userDto, BindingResult errors) {

        if (errors.hasErrors()) {
            throw new ContentNotAllowedException(errors.getAllErrors());
        }
        log.info("convert UserDto to User");
        User userRequest = UserMapper.INSTANCE.convertToUser(userDto);

        log.info("Save the user in database");
        User user = userService.registerNewUser(userRequest);

        log.info("convert User to UserDto");
        UserDto userResponse = UserMapper.INSTANCE.convertToDto(user);

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);

    }
}
