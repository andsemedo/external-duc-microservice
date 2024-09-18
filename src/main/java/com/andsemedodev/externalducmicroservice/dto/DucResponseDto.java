package com.andsemedodev.externalducmicroservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DucResponseDto(
        UUID idDuc,
        String recebedoria,
        String email,
        String nif,
        String moeda,
        Integer valorTotal,
        String obs,
        String codTransacao,
        String codTransacao1,
        Integer valor1,
        String codTransacao2,
        Integer valor2,
        List<RubricaResponseDto> rubricas,
        String instituicao,
        String departamento,
        String plataforma,
        String notas,
        LocalDateTime ducCreationDate
) {
}
