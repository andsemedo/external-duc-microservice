package com.andsemedodev.externalducmicroservice.service;

import com.andsemedodev.externalducmicroservice.dto.CreateDucResponseDto;
import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.dto.GetDucByNumberResponseDto;

import java.io.IOException;

public interface DucExternalService {
    CreateDucResponseDto createDuc(DucRequestDto requestDto);
    GetDucByNumberResponseDto getDucPdfByNumber(String ducNumber) throws IOException;
}
