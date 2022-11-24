package com.example.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Allows to handle exceptions across the whole application in one global handling component
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Customize the response for UserNotFoundException using an ErrorMessage.
     *
     * @param ex      UserNotFoundException
     * @param request WebRequest
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundException(UserNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false));
        log.error(ex.getClass().getSimpleName() + " due to " + ex.getMessage());
        log.error("handleUserNotFoundException: -> " + message);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    /**
     * Customize the response for ContentNotAllowedException using an ErrorMessage.
     *
     * @param ex ContentNotAllowedException
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(ContentNotAllowedException.class)
    public ResponseEntity<ErrorMessage> contentNotAllowedException(ContentNotAllowedException ex) {
        List<String> errorMessages = ex.getErrors()
                .stream()
                .map(contentError -> contentError.getObjectName() + " " + contentError.getDefaultMessage())
                .collect(toList());
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                errorMessages,
                "Invalid Constraints");
        log.error("contentNotAllowedException: -> " + message);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Customize the response for DataIntegrityViolationException using an ErrorMessage.
     * This exception is thrown when there is a violation of an integrity constraint (i.e: unique username).
     *
     * @param ex      DataIntegrityViolationException
     * @param request WebRequest
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> constraintViolation(DataIntegrityViolationException ex, WebRequest request) {
        log.error("Constraint violation exception : {}", ex.getMessage());
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false));
        log.error(ex.getClass().getSimpleName() + " due to " + ex.getMessage());
        log.error("handleConstraintViolation: -> " + message);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Customize the response for ValidationException using an ErrorMessage.
     *
     * @param e       ValidationException
     * @param request WebRequest
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> validationException(ValidationException e, WebRequest request) {
        log.error(String.valueOf(e));
        List<String> messages = new ArrayList<>();
        messages.add(e.getMessage());
        messages.add("Birthdate and country must not be nullable!");
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                messages,
                request.getDescription(false));
        log.error(e.getClass().getSimpleName() + " due to " + e.getMessage() + " => Birthdate and country must not be nullable! ");
        log.error("validationException: -> " + message);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * A single place to customize the response body of all Exception types.
     *
     * @param ex      Exception
     * @param request WebRequest: The current request
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        if (log.isWarnEnabled()) {
            log.warn("Unknown exception type: " + ex.getClass().getName());
        }
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                Collections.singletonList(ex.getMessage()),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
