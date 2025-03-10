package com.andsemedodev.externalducmicroservice.dto;

public record CreateDucResponseDto(
        String duc,
        String codigo,
        String descricao,
        String entidade,
        String referencia,
        String montante
) {
}
