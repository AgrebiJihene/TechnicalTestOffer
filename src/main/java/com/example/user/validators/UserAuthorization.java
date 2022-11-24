package com.example.user.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A custom validator to enforce the validation rules: Adult and French residents
 *
 * @interface to define our annotation
 */
@Constraint(validatedBy = UserAuthorizationValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAuthorization {
    String message() default "User is not authorized to create an account";

    String country();

    String birthdate();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
