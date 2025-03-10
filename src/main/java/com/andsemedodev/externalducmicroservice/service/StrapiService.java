package com.andsemedodev.externalducmicroservice.service;

import com.andsemedodev.externalducmicroservice.dto.RubricasDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StrapiService {
    List<String> fetchRubricasInRecebedoria(String codRecebedoria, List<RubricasDto> rubricasDtoList);
}
