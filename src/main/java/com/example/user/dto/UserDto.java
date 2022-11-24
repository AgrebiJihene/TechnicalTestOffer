package com.example.user.dto;

import com.example.user.domain.Gender;
import com.example.user.validators.UserAuthorization;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * This Data Transfer Object is used to transfer data from one processor context to another
 */
@Data
@UserAuthorization(
        country = "country",
        birthdate = "birthdate",
        message = "Only adult French residents are allowed to create an account"
)
public class UserDto {
    private Long id;
    @Size(min = 2, message = "user name should have at least 2 characters")
    @NotNull
    private String username;
    @Past
    @NotNull(message = "Please enter birthdate")
    private LocalDate birthdate;
    @NotNull
    private String country;
    private String phone;
    private Gender gender;

    public UserDto() {
    }
}
