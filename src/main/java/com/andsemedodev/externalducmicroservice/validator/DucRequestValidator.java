package com.andsemedodev.externalducmicroservice.validator;

import com.andsemedodev.externalducmicroservice.dto.DucTransactionRequestDto;
import com.andsemedodev.externalducmicroservice.dto.Rubricas;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class DucRequestValidator implements ConstraintValidator<ValidDucRequest, DucTransactionRequestDto> {
    @Override
    public boolean isValid(DucTransactionRequestDto dto, ConstraintValidatorContext context) {
        boolean isValid = true;

        // Desabilitar as mensagens de erro padrão
        context.disableDefaultConstraintViolation();

        if (Boolean.FALSE.equals(dto.getFlagCreationType())) {
            // Verificar se rubricas é obrigatório
            if (dto.getRubricas() == null || dto.getRubricas().isEmpty()) {
                context.buildConstraintViolationWithTemplate("Rubricas são obrigatórias quando flagCreationType é false.")
                        .addPropertyNode("rubricas")
                        .addConstraintViolation();
                isValid = false;
            } else {
                // Verificar se soma total dos valores de rubricas é igual a pValor
                int sumRubricasValor = dto.getRubricas().stream()
                        .filter(Objects::nonNull)
                        .mapToInt(Rubricas::valor)  // Presumindo que o método correto é getValor()
                        .sum();

                if (!Objects.equals(dto.getpValor(), sumRubricasValor)) {
                    context.buildConstraintViolationWithTemplate("pValor deve ser igual à soma total dos valores das rubricas.")
                            .addPropertyNode("pValor")
                            .addConstraintViolation();
                    isValid = false;
                }
            }

            // Verificar se os campos de transação não estão preenchidos
            if (dto.getpCodTransacao() != null) {
                context.buildConstraintViolationWithTemplate("pCodTransacao não deve ser preenchido.")
                        .addPropertyNode("pCodTransacao")
                        .addConstraintViolation();
                isValid = false;
            }
            if (dto.getpCodTransacao1() != null) {
                context.buildConstraintViolationWithTemplate("pCodTransacao1 não deve ser preenchido.")
                        .addPropertyNode("pCodTransacao1")
                        .addConstraintViolation();
                isValid = false;
            }
            if (dto.getpValor1() != null) {
                context.buildConstraintViolationWithTemplate("pValor1 não deve ser preenchido.")
                        .addPropertyNode("pValor1")
                        .addConstraintViolation();
                isValid = false;
            }
            if (dto.getpCodTransacao2() != null) {
                context.buildConstraintViolationWithTemplate("pCodTransacao2 não deve ser preenchido.")
                        .addPropertyNode("pCodTransacao2")
                        .addConstraintViolation();
                isValid = false;
            }
            if (dto.getpValor2() != null) {
                context.buildConstraintViolationWithTemplate("pValor2 não deve ser preenchido.")
                        .addPropertyNode("pValor2")
                        .addConstraintViolation();
                isValid = false;
            }

        } else if (Boolean.TRUE.equals(dto.getFlagCreationType())) {
            // Verificar se rubricas não foi passado
            if (dto.getRubricas() != null && !dto.getRubricas().isEmpty()) {
                context.buildConstraintViolationWithTemplate("Rubricas não devem ser passadas quando flagCreationType é true.")
                        .addPropertyNode("rubricas")
                        .addConstraintViolation();
                isValid = false;
            }

            // Verificar se a soma de pValor1 e pValor2 é igual a pValor
            int pValor1 = dto.getpValor1();
            int pValor2 = dto.getpValor2();
            int sum = pValor1 + pValor2;
            if (!Objects.equals(dto.getpValor(), sum)) {
                context.buildConstraintViolationWithTemplate("A soma de pValor1 e pValor2 deve ser igual a pValor.")
                        .addPropertyNode("pValor")
                        .addConstraintViolation();
                isValid = false;
            }
        }

        return isValid;
    }
}
