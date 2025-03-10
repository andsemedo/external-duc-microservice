package com.andsemedodev.externalducmicroservice.utilities;


import com.andsemedodev.externalducmicroservice.dto.CreateDucResponseDto;
import com.andsemedodev.externalducmicroservice.dto.GetDucByNumberResponseDto;

public class SwaggerDocResponses {
    public static class CreateDucResponseDtoSwagger extends APIResponse<CreateDucResponseDto> {}
    public static class GetDucByNumberResponseDtoSwagger extends APIResponse<GetDucByNumberResponseDto> {}
    public static class ExceptionResponseSwagger extends APIResponse<String> {}
}
