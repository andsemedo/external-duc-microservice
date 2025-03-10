package com.andsemedodev.externalducmicroservice.controller;

import com.andsemedodev.externalducmicroservice.dto.CreateDucResponseDto;
import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.dto.DucResponseDto;
import com.andsemedodev.externalducmicroservice.dto.GetDucByNumberResponseDto;
import com.andsemedodev.externalducmicroservice.utilities.APIResponse;
import com.andsemedodev.externalducmicroservice.service.DucExternalService;
import com.andsemedodev.externalducmicroservice.utilities.SwaggerDocResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/duc")
@SecurityRequirement(name = "basicAuth")
public class DucExternalController {
    private final DucExternalService ducExternalService;

    public DucExternalController(DucExternalService ducExternalService) {
        this.ducExternalService = ducExternalService;
    }

    @Operation(summary = "generate duc")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "CREATED",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SwaggerDocResponses.CreateDucResponseDtoSwagger.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "BAD REQUEST",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SwaggerDocResponses.ExceptionResponseSwagger.class))
            ),
            @ApiResponse(
                    responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SwaggerDocResponses.ExceptionResponseSwagger.class))
            ),
            @ApiResponse(
                    responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SwaggerDocResponses.ExceptionResponseSwagger.class))
            ),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<CreateDucResponseDto> createDuc(@Valid @RequestBody DucRequestDto requestDto) {


        if (requestDto.getFlagIsByTransacao()) {
            return new APIResponse.buildAPIResponse<CreateDucResponseDto>()
                    .setStatus(true)
                    .setStatusText(HttpStatus.CREATED.name())
                    .setDetails(ducExternalService.createDucByTransacao(requestDto))
                    .builder();
        } else {
            return new APIResponse.buildAPIResponse<CreateDucResponseDto>()
                    .setStatus(true)
                    .setStatusText(HttpStatus.CREATED.name())
                    .setDetails(ducExternalService.createDucByArrayIdRubrica(requestDto))
                    .builder();
        }

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<List<DucResponseDto>> getAllDucs() {
        return new APIResponse.buildAPIResponse<List<DucResponseDto>>()
                .setStatus(true)
                .setStatusText(HttpStatus.OK.name())
                .setDetails(ducExternalService.getAllDucs())
                .builder();
    }

}
