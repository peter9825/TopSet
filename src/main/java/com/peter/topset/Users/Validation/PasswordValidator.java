package com.peter.topset.Users.Validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordRequirements,String> {


    public static final int MIN_LENGTH = 8;

    public static final int MAX_LENGTH = 40;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        boolean valid = true;

        if (password == null || password.isBlank()){
            return true;
        }

        context.disableDefaultConstraintViolation();

        if (containsWhitespace(password)){
            valid = false;
            context.buildConstraintViolationWithTemplate("Password cannot contain whitespace.")
                    .addConstraintViolation();
        }


        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH){
            valid = false;
            context.buildConstraintViolationWithTemplate("Password length must be between 8 and 20 characters long")
                    .addConstraintViolation();
        }
        if (!containsDigit(password)){
            valid = false;
            context.buildConstraintViolationWithTemplate("Password must contain at least one digit.")
                    .addConstraintViolation();
        }

        if (!containsUpperCase(password)){
            valid = false;
            context.buildConstraintViolationWithTemplate("Password must contain at least one uppercase character.")
                    .addConstraintViolation();
        }

        if (!containsNonAlphaNumeric(password)){
            valid = false;
            context.buildConstraintViolationWithTemplate("Password must contain at least one special character.")
                    .addConstraintViolation();
        }

        return valid;
    }

public boolean containsNonAlphaNumeric(String password){
    for (int i = 0; i < password.length(); i++) {
        char c = password.charAt(i);
        if (!Character.isLetterOrDigit(c)){
            return true;
        }
    }
    return false;
}

public boolean containsDigit(String password){
    for (int i = 0; i < password.length(); i++){
        char c = password.charAt(i);
        if (Character.isDigit(c)){
            return true;
        }
    }
    return false;
}

public boolean containsUpperCase(String password){
    for (int i = 0; i < password.length(); i++) {
        char c = password.charAt(i);
        if (Character.isUpperCase(c)){
            return true;
        }
    }
    return false;
}

public boolean containsWhitespace(String password){
    for (int i = 0; i < password.length(); i++) {
        char c = password.charAt(i);
        if (Character.isWhitespace(c)){
            return true;
        }
    }
    return false;
}

}
