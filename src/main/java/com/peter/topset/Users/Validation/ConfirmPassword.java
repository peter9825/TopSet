package com.peter.topset.Users.Validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConfirmPasswordValidator.class)
@Target({ElementType.PARAMETER,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmPassword {

    String message() default "Passwords must match.";
    Class<?>[] groups() default {};
    Class<?extends Payload>[] payload() default {};
}
