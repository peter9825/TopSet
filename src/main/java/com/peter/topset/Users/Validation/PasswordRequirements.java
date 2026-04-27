package com.peter.topset.Users.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordRequirements {
    String message() default "Password does not satisfy requirements.";

            Class<?>[] groups() default {};
            Class<?extends Payload>[] payload() default {};
}
