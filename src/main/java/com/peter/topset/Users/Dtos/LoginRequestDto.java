package com.peter.topset.Users.Dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotNull
    @NotBlank
    String email;

    @NotNull
    @NotBlank
    String password;

    public LoginRequestDto(String email, String password){
        this.email = email;
        this.password = password;
    }
}
