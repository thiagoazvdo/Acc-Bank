package com.accenture.academico.Acc.Bank.validation.validator;

import com.accenture.academico.Acc.Bank.validation.ValidEmail;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String EMAIL_PATTERN = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w+$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return true; 
        }
        return email.matches(EMAIL_PATTERN);
    }
}
