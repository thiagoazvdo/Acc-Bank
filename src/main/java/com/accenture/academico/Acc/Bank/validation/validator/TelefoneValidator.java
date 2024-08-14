package com.accenture.academico.Acc.Bank.validation.validator;

import com.accenture.academico.Acc.Bank.validation.ValidTelefone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class TelefoneValidator implements ConstraintValidator<ValidTelefone, String> {

    private static final String TELEFONE_PATTERN = "\\d{11}";

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return true; 
        }

        return telefone.matches(TELEFONE_PATTERN);
    }
}