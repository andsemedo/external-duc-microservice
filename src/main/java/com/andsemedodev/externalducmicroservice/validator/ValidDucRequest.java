package com.andsemedodev.externalducmicroservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DucRequestValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDucRequest {
    String message() default "Invalid Duc Request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
