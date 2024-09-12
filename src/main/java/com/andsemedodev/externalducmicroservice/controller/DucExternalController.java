package com.andsemedodev.externalducmicroservice.controller;

import com.andsemedodev.externalducmicroservice.dto.CreateDucResponseDto;
import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.dto.GetDucByNumberResponseDto;
import com.andsemedodev.externalducmicroservice.utilities.APIResponse;
import com.andsemedodev.externalducmicroservice.service.DucExternalService;
import com.andsemedodev.externalducmicroservice.utilities.SwaggerDocResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

@CrossOrigin
@RestController
@RequestMapping("/duc")
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
        return new APIResponse.buildAPIResponse<CreateDucResponseDto>()
                .setStatus(true)
                .setStatusText(HttpStatus.CREATED.name())
                .setDetails(ducExternalService.createDuc(requestDto))
                .builder();
    }

    @Operation(summary = "get duc")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "SUCCESS",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SwaggerDocResponses.GetDucByNumberResponseDtoSwagger.class))
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
                    responseCode = "404", description = "NOT_FOUND",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SwaggerDocResponses.ExceptionResponseSwagger.class))
            ),
            @ApiResponse(
                    responseCode = "500", description = "INTERNAL SERVER ERROR",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SwaggerDocResponses.ExceptionResponseSwagger.class))
            ),
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public APIResponse<GetDucByNumberResponseDto> getDucPdfByNumber(@NotNull @RequestParam(value = "ducNumber") String ducNumber) {
        try {
            return new APIResponse.buildAPIResponse<GetDucByNumberResponseDto>()
                    .setStatus(true)
                    .setStatusText(HttpStatus.OK.name())
                    .setDetails(ducExternalService.getDucPdfByNumber(ducNumber))
                    .builder();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
