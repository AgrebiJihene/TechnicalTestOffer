package com.example.user.exception;

/**
 * this exception is thrown when trying to display a non-existent user
 */
public class UserNotFoundException extends RuntimeException {
    private final long id;

    public UserNotFoundException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "User '" + id + "' not found";
    }
}