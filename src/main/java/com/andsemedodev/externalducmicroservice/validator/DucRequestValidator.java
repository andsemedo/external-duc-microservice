package com.andsemedodev.externalducmicroservice.validator;

import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.dto.RubricasDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class DucRequestValidator implements ConstraintValidator<ValidDucRequest, DucRequestDto> {
    @Override
    public boolean isValid(DucRequestDto dto, ConstraintValidatorContext context) {
        boolean isValid = true;

        // disable the standard error messages
        context.disableDefaultConstraintViolation();

        // verify if it's by transacao
        if (dto.getFlagIsByTransacao()) {
            if (dto.getpCodTransacao() == null) {
                context.buildConstraintViolationWithTemplate("na criação de DUC por transação, pCodTransacao não pode ser null ou vazio")
                        .addPropertyNode("error")
                        .addConstraintViolation();
                isValid = false;
            }
            if (dto.getpCodTransacao1() == null && dto.getpCodTransacao2() == null) {
                context.buildConstraintViolationWithTemplate("na criação de DUC por transação, pCodTransacao1 ou pCodTransacao2 devem ser preenchidos")
                        .addPropertyNode("error")
                        .addConstraintViolation();
                isValid = false;
            }

            int sumValores = dto.getpValor1() + dto.getpValor2();
            if (dto.getpValor() != sumValores) {
                context.buildConstraintViolationWithTemplate("na criação de DUC por transação, a soma de pValor1 e pValor2 deve ser igual a pValor")
                        .addPropertyNode("error")
                        .addConstraintViolation();
                isValid = false;
            }

        } else {
            // verify if rubricas is filled out
            if (dto.getRubricas() == null || dto.getRubricas().isEmpty()) {
                context.buildConstraintViolationWithTemplate("na criação de DUC por array ID, rubricas não pode ser null ou vazio")
                        .addPropertyNode("error")
                        .addConstraintViolation();
                isValid = false;
            } else {
                // Verificar se soma total dos valores de rubricas é igual a pValor
                int sumRubricasValor = dto.getRubricas().stream()
                        .filter(Objects::nonNull)
                        .mapToInt(RubricasDto::valor)  // Presumindo que o método correto é getValor()
                        .sum();

                if (!Objects.equals(dto.getpValor(), sumRubricasValor)) {
                    context.buildConstraintViolationWithTemplate("na criação de DUC por array ID, A soma total dos valores das rubricas deve ser igual a pValor")
                            .addPropertyNode("error")
                            .addConstraintViolation();
                    isValid = false;
                }
            }
        }

        return isValid;
    }
}
