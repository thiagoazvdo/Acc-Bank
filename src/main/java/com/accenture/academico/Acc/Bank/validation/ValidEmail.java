package com.accenture.academico.Acc.Bank.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.accenture.academico.Acc.Bank.validation.validator.EmailValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = EmailValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "Email inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

