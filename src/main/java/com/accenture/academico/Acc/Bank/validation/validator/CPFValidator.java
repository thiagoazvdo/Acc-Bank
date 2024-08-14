package com.accenture.academico.Acc.Bank.validation.validator;

import com.accenture.academico.Acc.Bank.validation.ValidCPF;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<ValidCPF, String> {

    private static final String CPF_PATTERN = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return true; 
        }

        return cpf.matches(CPF_PATTERN);
    }
}
