package com.andsemedodev.externalducmicroservice.controller;

import com.andsemedodev.externalducmicroservice.dto.CreateDucResponseDto;
import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.service.DucExternalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/duc")
public class DucExternalController {
    private final DucExternalService ducExternalService;

    public DucExternalController(DucExternalService ducExternalService) {
        this.ducExternalService = ducExternalService;
    }

    @PostMapping
    public ResponseEntity<CreateDucResponseDto> createDuc(@Valid @RequestBody DucRequestDto requestDto) {
        return ResponseEntity.ok(ducExternalService.createDuc(requestDto));
    }

}
