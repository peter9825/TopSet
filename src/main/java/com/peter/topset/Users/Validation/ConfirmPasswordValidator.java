package com.peter.topset.Users.Validation;

import com.peter.topset.Users.Dtos.CreateUserRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, CreateUserRequestDto> {

@Override
public boolean isValid(CreateUserRequestDto dto, ConstraintValidatorContext context){

String password = dto.getPassword();
String confirmPassword = dto.getConfirmPassword();

    if (password == null || password.isBlank()){
        return true;
    }

    if (confirmPassword == null){
        return true;
    }

    return password.equals(confirmPassword);

}


}
