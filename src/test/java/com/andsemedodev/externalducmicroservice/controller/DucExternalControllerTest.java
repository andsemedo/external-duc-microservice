package com.andsemedodev.externalducmicroservice.controller;

import com.andsemedodev.externalducmicroservice.dto.*;
import com.andsemedodev.externalducmicroservice.service.DucExternalService;
import com.andsemedodev.externalducmicroservice.utilities.APIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
public class DucExternalControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private DucExternalService ducExternalService;

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;
    @Value("${auth.username}")
    private String username;
    @Value("${auth.password}")
    private String password;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext).apply(springSecurity())
                        .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void createDuc_ShouldReturnCreatedResponse_WhenFlagIsByArrayIdRubrica() throws Exception {
        // ...Given
        DucRequestDto requestDto = new DucRequestDto();
        requestDto.setpValor(1600);
        requestDto.setpRecebedoria("299");
        requestDto.setpNif(BigInteger.valueOf(123792479));
        requestDto.setpObs("Some Obs");
        requestDto.setpCodTransacao(null);
        requestDto.setpCodTransacao1(null);
        requestDto.setpValor1(null);
        requestDto.setpCodTransacao2(null);
        requestDto.setpValor2(null);
        RubricasDto rubrica1 = new RubricasDto(1, 1350);
        RubricasDto rubrica2 = new RubricasDto(2, 250);
        requestDto.setRubricas(List.of(rubrica1, rubrica2));
        requestDto.setInstituicao("Policia Nacional");
        requestDto.setDepartamento("DEF");
        requestDto.setPlataforma("SARBA");
        requestDto.setNotas("Any notes");
        requestDto.setFlagIsByTransacao(false);

        CreateDucResponseDto responseDto = new CreateDucResponseDto(
                "920000118489", "-", "OPERACAO REALIZADA COM SUCESSO",
                "92000", "011848933", "1750"
        );

        when(ducExternalService.createDucByArrayIdRubrica(any(DucRequestDto.class)))
                .thenReturn(responseDto);

        // ...When / ...Then
        mockMvc.perform(post("/duc")
//                        .with(httpBasic(username, password))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new APIResponse.buildAPIResponse<CreateDucResponseDto>()
                                .setStatus(true)
                                .setStatusText("CREATED")
                                .setDetails(responseDto)
                                .builder()
                )));
    }

    @Test
    @WithMockUser
    void createDuc_ShouldReturnCreatedResponse_WhenFlagIsByTransacao() throws Exception {
        // ...Given
        DucRequestDto requestDto = new DucRequestDto();
        requestDto.setpValor(1600);
        requestDto.setpRecebedoria("299");
        requestDto.setpNif(BigInteger.valueOf(123792479));
        requestDto.setpObs("Some Obs");
        requestDto.setpCodTransacao("CDTRANSDUPLO");
        requestDto.setpCodTransacao1("MCF01");
        requestDto.setpValor1(1350);
        requestDto.setpCodTransacao2("MCF02");
        requestDto.setpValor2(250);
        requestDto.setRubricas(List.of());
        requestDto.setInstituicao("Policia Nacional");
        requestDto.setDepartamento("DEF");
        requestDto.setPlataforma("SARBA");
        requestDto.setNotas("Any notes");
        requestDto.setFlagIsByTransacao(true);

        CreateDucResponseDto responseDto = new CreateDucResponseDto(
                "920000118489", "-", "OPERACAO REALIZADA COM SUCESSO",
                "92000", "011848933", "1750"
        );

        when(ducExternalService.createDucByTransacao(any(DucRequestDto.class)))
                .thenReturn(responseDto);

        // ...When / ...Then
        mockMvc.perform(post("/duc")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new APIResponse.buildAPIResponse<CreateDucResponseDto>()
                                .setStatus(true)
                                .setStatusText("CREATED")
                                .setDetails(responseDto)
                                .builder()
                )));
    }

    @Test
    @WithMockUser
    void getAllDucs_ShouldReturnListOfDucResponseDto() throws Exception {
        // ...Given
        DucResponseDto ducResponse1 = new DucResponseDto(
                UUID.randomUUID(), "299", "celina@zing.cv", "123792479", "CVE",
                1600, "Some Obs", "CDTRANSDUPLO", "MCF01", 1350,
                "MCF02", 250, List.of(), "Policia Nacional", "DEF",
                "SARBA","Any notes", LocalDateTime.now()
        );
        RubricaResponseDto rubrica1 = new RubricaResponseDto("255", 1350);
        RubricaResponseDto rubrica2 = new RubricaResponseDto("254", 250);
        DucResponseDto ducResponse2 = new DucResponseDto(
                UUID.randomUUID(), "299", "celina@zing.cv", "123792479", "CVE",
                1600, "Some Obs", null, null, null,
                null, null, List.of(rubrica1, rubrica2), "Policia Nacional", "DEF",
                "SARBA","Any notes", LocalDateTime.now()
        );

        when(ducExternalService.getAllDucs())
                .thenReturn(List.of(ducResponse1, ducResponse2));

        // ...When / ...Then
        mockMvc.perform(get("/duc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new APIResponse.buildAPIResponse<List<DucResponseDto>>()
                                .setStatus(true)
                                .setStatusText("OK")
                                .setDetails(List.of(ducResponse1, ducResponse2))
                                .builder()
                )));

    }
}
