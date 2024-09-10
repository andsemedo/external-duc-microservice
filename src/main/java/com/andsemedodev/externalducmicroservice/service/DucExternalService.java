package com.andsemedodev.externalducmicroservice.service;

import com.andsemedodev.externalducmicroservice.dto.CreateDucResponseDto;
import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.service.impl.responses.GenerateDucResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface DucExternalService {
    CreateDucResponseDto createDuc(DucRequestDto requestDto);
}
