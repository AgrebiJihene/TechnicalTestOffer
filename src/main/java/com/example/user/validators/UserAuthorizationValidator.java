package com.example.user.validators;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * The validator class that is going to validate our fields: country and birthdate
 */
public class UserAuthorizationValidator implements ConstraintValidator<UserAuthorization, Object> {
    private String country;
    private String birthdate;

    public void initialize(UserAuthorization annotation) {
        this.country = annotation.country();
        this.birthdate = annotation.birthdate();
    }

    /**
     * This method checks if the user is authorized to create an account. It calculates the age and checks if the country is France.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return boolean
     */
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object countryValue = new BeanWrapperImpl(value)
                .getPropertyValue(country);
        Object birthdateValue = new BeanWrapperImpl(value)
                .getPropertyValue(birthdate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth;
        if (birthdateValue != null)
            birth = LocalDate.parse(birthdateValue.toString(), formatter);
        else
            throw new ValidationException();
        int age = Period.between(birth, LocalDate.now()).getYears();
        if (countryValue != null)
            return (countryValue.toString().equalsIgnoreCase("FRANCE")) && age > 18;
        else
            throw new ValidationException();
    }
}
