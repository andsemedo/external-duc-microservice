package com.andsemedodev.externalducmicroservice.service;

import com.andsemedodev.externalducmicroservice.dto.CreateDucResponseDto;
import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.dto.RubricasDto;
import com.andsemedodev.externalducmicroservice.exceptions.CustomInternalServerErrorException;
import com.andsemedodev.externalducmicroservice.model.Duc;
import com.andsemedodev.externalducmicroservice.model.DucRubrica;
import com.andsemedodev.externalducmicroservice.repository.DucRepository;
import com.andsemedodev.externalducmicroservice.repository.DucRubricaRepository;
import com.andsemedodev.externalducmicroservice.service.impl.DucExternalServiceImpl;
import com.andsemedodev.externalducmicroservice.service.impl.responses.DucByIdRubricas;
import com.andsemedodev.externalducmicroservice.service.impl.responses.DucByTransacaoResponse;
import com.andsemedodev.externalducmicroservice.service.impl.responses.DucPSaida;
import com.andsemedodev.externalducmicroservice.service.impl.responses.GenerateDucResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("dev")
class DucExternalServiceImplTest {

    private static MockWebServer mockWebServerRubrica;
    private static MockWebServer mockWebServerTransacao;

    @MockBean
    private DucRepository ducRepository;

    @MockBean
    private DucRubricaRepository ducRubricaRepository;

    @Autowired
    private Environment env;

    @Autowired
    private DucExternalServiceImpl ducExternalService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServerRubrica = new MockWebServer();
        mockWebServerTransacao = new MockWebServer();
        mockWebServerRubrica.start();
        mockWebServerTransacao.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServerRubrica.shutdown();
        mockWebServerTransacao.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrlRubrica = String.format("http://localhost:%s", mockWebServerRubrica.getPort());
        String baseUrlTransacao = String.format("http://localhost:%s", mockWebServerTransacao.getPort());

        ReflectionTestUtils.setField(ducExternalService, "byRubricaUrl", baseUrlRubrica + "/t/financas.gov/rubricaidduc/1.0.0/processBancaArrayId");
        ReflectionTestUtils.setField(ducExternalService, "byTransacaoUrl", baseUrlTransacao + "/t/financas.gov/mfservicesduccreatev3/1.0.3/postProcessBanca");

        WebClient webClient = WebClient.builder().build();
        ReflectionTestUtils.setField(ducExternalService, "webClient", webClient);
    }

    @Test
    void createDucByTransacao_Success() throws JsonProcessingException {
        // Arrange
        DucByTransacaoResponse mockResponse = new DucByTransacaoResponse();
        DucPSaida pSaida = new DucPSaida();
        pSaida.setpSaida("<DUC>920000118489</DUC><CODIGO></CODIGO><DESCRICAO>OPERACAO REALIZADA COM SUCESSO</DESCRICAO><ENTIDADE>92000</ ENTIDADE><REFERENCIA>011848933</REFERENCIA><MONTANTE>1600</MONTANTE>");
        mockResponse.setPSaida(pSaida);

        String mockResponseBody = objectMapper.writeValueAsString(mockResponse);

        mockWebServerTransacao.enqueue(new MockResponse()
                .setBody(mockResponseBody)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        DucRequestDto requestDto = new DucRequestDto();
        requestDto.setpValor(1600);
        requestDto.setpRecebedoria("299");
        requestDto.setpNif(BigInteger.valueOf(123792479));
        requestDto.setpObs("Some Obs");
        requestDto.setpCodTransacao("CDTRANSDUPLO");
        requestDto.setpCodTransacao1("MCAII01");
        requestDto.setpValor1(1250);
        requestDto.setpCodTransacao2("MCAII02");
        requestDto.setpValor2(350);
        requestDto.setRubricas(null);
        requestDto.setInstituicao("Policia Nacional");
        requestDto.setDepartamento("DEF");
        requestDto.setPlataforma("SARBA");
        requestDto.setNotas("Any notes");
        requestDto.setFlagIsByTransacao(true);
        // Act
        CreateDucResponseDto result = ducExternalService.createDucByTransacao(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals("1600", result.montante());

        verify(ducRepository).save(any(Duc.class));
    }

    @Test
    void createDucByTransacao_ErrorResponse() {
        // Arrange
        mockWebServerTransacao.enqueue(new MockResponse().setResponseCode(500));

        DucRequestDto requestDto = new DucRequestDto();

        // Act & Assert
        assertThrows(CustomInternalServerErrorException.class, () -> ducExternalService.createDucByTransacao(requestDto));
    }

//    @Test
//    void createDucByArrayIdRubrica_ErrorResponse() throws JsonProcessingException {
//        // Arrange
//        String mockResponseBody = objectMapper.writeValueAsString(new GenerateDucResponse());
//        mockWebServerTransacao.enqueue(new MockResponse()
//                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .setBody("Internal Server Error"));
//
//        DucRequestDto requestDto = new DucRequestDto();
//        requestDto.setpValor(1600);
//        requestDto.setpNif(BigInteger.valueOf(123792479));
////        requestDto.setpObs("Some Obs");
//        RubricasDto rubrica1 = new RubricasDto(1, 1350);
//        RubricasDto rubrica2 = new RubricasDto(2, 250);
//        requestDto.setRubricas(List.of(rubrica1, rubrica2));
////        requestDto.setFlagIsByTransacao(false);
//        when(ducExternalService.createDucByArrayIdRubrica(requestDto)).thenReturn();
//        // Act & Assert
//        assertThrows(CustomInternalServerErrorException.class, () -> ducExternalService.createDucByArrayIdRubrica(requestDto));
//    }

    @Test
    void createDucByArrayIdRubrica_Success() throws JsonProcessingException {
        // Arrange
        GenerateDucResponse mockResponse = new GenerateDucResponse();
        DucByIdRubricas ducByIdRubricas = new DucByIdRubricas();
        DucPSaida pSaida = new DucPSaida();
        pSaida.setpSaida("""
                {
                \"ducByIdRubricas\": {
                    \"ducByIdRubrica\": {
                        \"p_saida\":\"<DUC>921001612255</DUC><CODIGO>-</CODIGO><DESCRICAO>OPERACAO REALIZADA COM SUCESSO</DESCRICAO><ENTIDADE>92100</ENTIDADE><REFERENCIA>161225533</REFERENCIA><MONTANTE>1600</MONTANTE>\"
                        }
                    }
                }
                """
        );
        ducByIdRubricas.setDucByIdRubrica(pSaida);
        mockResponse.setDucByIdRubricas(ducByIdRubricas);
        String mockResponseBody = objectMapper.writeValueAsString(mockResponse);

        mockWebServerRubrica.enqueue(new MockResponse()
                .setBody(mockResponseBody)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        DucRequestDto requestDto = new DucRequestDto();
        requestDto.setpValor(1600);
        requestDto.setpRecebedoria("299");
        requestDto.setpNif(BigInteger.valueOf(123792479));
        requestDto.setpObs("Some Obs");
        requestDto.setpCodTransacao("CDTRANSDUPLO");
        requestDto.setpCodTransacao1("MCAII01");
        requestDto.setpValor1(1250);
        requestDto.setpCodTransacao2("MCAII02");
        requestDto.setpValor2(350);
        RubricasDto rubrica1 = new RubricasDto(1, 1350);
        RubricasDto rubrica2 = new RubricasDto(2, 250);
        requestDto.setRubricas(List.of(rubrica1, rubrica2));
        requestDto.setInstituicao("Policia Nacional");
        requestDto.setDepartamento("DEF");
        requestDto.setPlataforma("SARBA");
        requestDto.setNotas("Any notes");
        requestDto.setFlagIsByTransacao(false);

        // Act
        CreateDucResponseDto result = ducExternalService.createDucByArrayIdRubrica(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals("1600", result.montante());

        verify(ducRepository).save(any(Duc.class));
        verify(ducRubricaRepository, times(2)).save(any(DucRubrica.class));
    }

    // Add more tests as needed...
}