package com.andsemedodev.externalducmicroservice.validator;

import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.dto.Rubricas;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class DucRequestValidator implements ConstraintValidator<ValidDucRequest, DucRequestDto> {
    @Override
    public boolean isValid(DucRequestDto dto, ConstraintValidatorContext context) {
        boolean isValid = true;

        // Desabilitar as mensagens de erro padrão
        context.disableDefaultConstraintViolation();

        // Verificar se rubricas é obrigatório
        if (dto.getRubricas() == null || dto.getRubricas().isEmpty()) {
            context.buildConstraintViolationWithTemplate("rubricas não pode ser null ou vazio")
                    .addPropertyNode("error")
                    .addConstraintViolation();
            isValid = false;
        } else {
            // Verificar se soma total dos valores de rubricas é igual a pValor
            int sumRubricasValor = dto.getRubricas().stream()
                    .filter(Objects::nonNull)
                    .mapToInt(Rubricas::valor)  // Presumindo que o método correto é getValor()
                    .sum();

            if (!Objects.equals(dto.getpValor(), sumRubricasValor)) {
                context.buildConstraintViolationWithTemplate("A soma total dos valores das rubricas deve ser igual a pValor")
                        .addPropertyNode("error")
                        .addConstraintViolation();
                isValid = false;
            }
        }
        return isValid;
    }
}
