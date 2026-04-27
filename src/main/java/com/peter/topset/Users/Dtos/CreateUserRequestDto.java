package com.peter.topset.Users.Dtos;

import com.peter.topset.Users.Validation.ConfirmPassword;
import com.peter.topset.Users.Validation.PasswordRequirements;
import jakarta.validation.constraints.*;
import lombok.Getter;


@Getter
@ConfirmPassword
public class CreateUserRequestDto {

    @NotNull
    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", flags = Pattern.Flag.CASE_INSENSITIVE
            ,message = "Email must be a valid email address.")
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters long.")
    private String username;

    @NotNull
    @NotBlank
    @PasswordRequirements
    private String password;

    @NotNull
    @NotBlank
    private String confirmPassword;


    public CreateUserRequestDto(String email, String username, String password, String confirmPassword){
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

}
