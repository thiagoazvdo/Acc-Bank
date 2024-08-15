package com.accenture.academico.Acc.Bank.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.accenture.academico.Acc.Bank.validation.validator.CPFValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = CPFValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCPF {
    String message() default "CPF inválido. O formato deve ser 000.000.000-00";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
