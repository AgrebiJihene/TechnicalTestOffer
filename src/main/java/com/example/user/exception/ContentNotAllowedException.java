package com.example.user.exception;

import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * This exception is thrown when there are invalid attributes when creating a user
 */
public class ContentNotAllowedException extends RuntimeException {
    private final List<ObjectError> errors;

    public ContentNotAllowedException(List<ObjectError> errors) {
        this.errors = errors;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }


}
