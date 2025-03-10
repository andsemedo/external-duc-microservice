package com.andsemedodev.externalducmicroservice.service;

import com.andsemedodev.externalducmicroservice.dto.CreateDucResponseDto;
import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.dto.DucResponseDto;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    private DucExternalServiceImpl underTest;

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

        ReflectionTestUtils.setField(underTest, "byRubricaUrl", baseUrlRubrica + "/t/financas.gov/rubricaidduc/1.0.0/processBancaArrayId");
        ReflectionTestUtils.setField(underTest, "byTransacaoUrl", baseUrlTransacao + "/t/financas.gov/mfservicesduccreatev3/1.0.3/postProcessBanca");

        WebClient webClient = WebClient.builder().build();
        ReflectionTestUtils.setField(underTest, "webClient", webClient);
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
        CreateDucResponseDto result = underTest.createDucByTransacao(requestDto);

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
        assertThrows(CustomInternalServerErrorException.class, () -> underTest.createDucByTransacao(requestDto));
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

        // Act
        CreateDucResponseDto result = underTest.createDucByArrayIdRubrica(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals("1600", result.montante());

        verify(ducRepository).save(any(Duc.class));
        verify(ducRubricaRepository, times(2)).save(any(DucRubrica.class));
    }

    @Test
    void getAllDucs_Success() {
        // ...Given
        Duc duc = new Duc();
        duc.setId(UUID.randomUUID());
        duc.setpValor(1600);
        duc.setpMoeda("CVE");
        duc.setpRecebedoria("299");
        duc.setpEmail("celina@mf.gov.cv");
        duc.setpNif(BigInteger.valueOf(123792479));
        duc.setpObs("Some Obs");
        duc.setpCodTransacao("CDTRANSDUPLO");
        duc.setpCodTransacao1("MIF01");
        duc.setpValor1(1350);
        duc.setpCodTransacao2("MIF02");
        duc.setpValor2(250);
        duc.setInstituicao("PN");
        duc.setDepartamento("DEF");
        duc.setPlataforma("SARBA");
        duc.setNotas("Some notes");
        duc.setRubricaList(List.of());
        duc.setCreatedAt(LocalDateTime.now());
        duc.setUpdatedAt(LocalDateTime.now());

        Duc duc1 = new Duc();
        duc1.setId(UUID.randomUUID());
        duc1.setpValor(1600);
        duc1.setpMoeda("CVE");
        duc1.setpRecebedoria("299");
        duc1.setpEmail("celina@mf.gov.cv");
        duc1.setpNif(BigInteger.valueOf(123792479));
        duc1.setpObs("Some Obs");
        duc1.setpCodTransacao(null);
        duc1.setpCodTransacao1(null);
        duc1.setpValor1(null);
        duc1.setpCodTransacao2(null);
        duc1.setpValor2(null);
        duc1.setInstituicao("PN");
        duc1.setDepartamento("DEF");
        duc1.setPlataforma("SARBA");
        duc1.setNotas("Some notes");
        DucRubrica ducRubrica = new DucRubrica();
        ducRubrica.setId(UUID.randomUUID());
        ducRubrica.setCodRubrica("CodRub01");
        ducRubrica.setValor(1350);
        ducRubrica.setCreatedAt(LocalDateTime.now());
        ducRubrica.setUpdatedAt(LocalDateTime.now());

        DucRubrica ducRubrica1 = new DucRubrica();
        ducRubrica1.setId(UUID.randomUUID());
        ducRubrica1.setCodRubrica("CodRub02");
        ducRubrica1.setValor(250);
        ducRubrica1.setCreatedAt(LocalDateTime.now());
        ducRubrica1.setUpdatedAt(LocalDateTime.now());
        duc1.setRubricaList(List.of(ducRubrica, ducRubrica1));
        duc1.setCreatedAt(LocalDateTime.now());
        duc1.setUpdatedAt(LocalDateTime.now());

        // ...When
        when(ducRepository.findAll()).thenReturn(List.of(duc, duc1));

        List<DucResponseDto> allDucs = underTest.getAllDucs();

        // ...Then
        assertNotNull(allDucs);
        assertEquals(2, allDucs.size());

    }

    // Add more tests as needed...
}