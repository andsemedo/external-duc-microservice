package com.andsemedodev.externalducmicroservice.service;

import com.andsemedodev.externalducmicroservice.dto.CreateDucResponseDto;
import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.dto.DucResponseDto;
import com.andsemedodev.externalducmicroservice.dto.GetDucByNumberResponseDto;

import java.io.IOException;
import java.util.List;

public interface DucExternalService {
    CreateDucResponseDto createDucByTransacao(DucRequestDto requestDto);
    CreateDucResponseDto createDucByArrayIdRubrica(DucRequestDto requestDto);
    List<DucResponseDto> getAllDucs();
}
