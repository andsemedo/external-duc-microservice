package com.andsemedodev.externalducmicroservice.service.impl;

import com.andsemedodev.externalducmicroservice.dto.*;
import com.andsemedodev.externalducmicroservice.exceptions.CustomInternalServerErrorException;
import com.andsemedodev.externalducmicroservice.exceptions.RecordNotFoundException;
import com.andsemedodev.externalducmicroservice.model.Duc;
import com.andsemedodev.externalducmicroservice.model.DucRubrica;
import com.andsemedodev.externalducmicroservice.repository.DucRepository;
import com.andsemedodev.externalducmicroservice.repository.DucRubricaRepository;
import com.andsemedodev.externalducmicroservice.service.impl.responses.DucByTransacaoResponse;
import com.andsemedodev.externalducmicroservice.service.impl.responses.GenerateDucResponse;
import com.andsemedodev.externalducmicroservice.service.impl.rubrica_request.ProcessBancaArrayId;
import com.andsemedodev.externalducmicroservice.exceptions.EmptyRubricasException;
import com.andsemedodev.externalducmicroservice.service.DucExternalService;
import com.andsemedodev.externalducmicroservice.service.impl.rubrica_request.RubricaRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DucExternalServiceImpl implements DucExternalService {
    private final Logger logger = Logger.getLogger(DucExternalServiceImpl.class.getName());

    private final WebClient webClient = WebClient.builder().build();
    private final ObjectMapper objectMapper;

    @Value("${duc.token}")
    private String token;
    @Value("${duc.pEmail}")
    private String pEmail;
    @Value("${duc.by-rubrica-url}")
    private String byRubricaUrl;
    @Value("${duc.by-transacao-url}")
    private String byTransacaoUrl;
    private final String moeda = "CVE";

    private final DucRepository ducRepository;
    private final DucRubricaRepository ducRubricaRepository;

    public DucExternalServiceImpl(ObjectMapper objectMapper, DucRepository ducRepository, DucRubricaRepository ducRubricaRepository) {
        this.objectMapper = objectMapper;
        this.ducRepository = ducRepository;
        this.ducRubricaRepository = ducRubricaRepository;
    }

    @Override
    public CreateDucResponseDto createDucByTransacao(DucRequestDto requestDto) {
        logger.info("Creating DUC By Transacao");
        // TODO - search for codTransacao1 e codTransacao2 in StrapiCMS
        // TODO - cache the strapi data

        ResponseEntity<DucByTransacaoResponse> response = createDucRequestByTransacao(byTransacaoUrl, requestDto);

        if (response.getStatusCode().is2xxSuccessful()) {
            // save duc info request in the DB
            saveDucInfoInDB(requestDto);
            logger.info("Request successful to pedex");
            DucByTransacaoResponse body = response.getBody();
            if (body != null) {
                String pSaida = body.getPSaida().getpSaida();
                return new CreateDucResponseDto(
                        extractXmlValue(pSaida, "DUC"),
                        extractXmlValue(pSaida, "CODIGO"),
                        extractXmlValue(pSaida, "DESCRICAO"),
                        extractXmlValue(pSaida, "ENTIDADE"),
                        extractXmlValue(pSaida, "REFERENCIA"),
                        extractXmlValue(pSaida, "MONTANTE")
                );
            }
            logger.info("Empty response body");
            throw new RecordNotFoundException("Empty response body");
        }

        logger.info("Error while generating duc");
        throw new CustomInternalServerErrorException("An error occurred while trying to create duc with status code: ");
    }

    @Override
    public CreateDucResponseDto createDucByArrayIdRubrica(DucRequestDto requestDto) {
        logger.info("Creating DUC By Array Id Rubrica");

        // TODO - search for rubricas in Strapi CMS by its ID and return cod rubrica
        // TODO - cache the strapi data

        // map p_id_rubricas
        List<RubricasDto> rubricas = requestDto.getRubricas();
        if (rubricas == null || rubricas.isEmpty())
            throw new EmptyRubricasException("Rubricas nao pode ser nulo ou vazio na criação de DUC por array id rubricas");
        StringBuilder idRubricas = new StringBuilder();
        StringBuilder vlRubricas = new StringBuilder();

        rubricas.forEach(r -> {
            idRubricas.append(r.id()).append(";");
            vlRubricas.append(r.valor()).append(";");
        });

        ProcessBancaArrayId processBancaArrayId = new ProcessBancaArrayId(
                requestDto.getpValor(), moeda, 223, pEmail,
                requestDto.getpNif(), requestDto.getpObs(), idRubricas.toString(), vlRubricas.toString()
        );

        ResponseEntity<GenerateDucResponse> result = createDucRequestByArrayIdRubricas(byRubricaUrl, new RubricaRequest(processBancaArrayId));

        if (result.getStatusCode().is2xxSuccessful()) {
            // save duc info request in the DB
            saveDucInfoInDB(requestDto);
            logger.info("Request successful to pedex");
            GenerateDucResponse ducResponse = result.getBody();
            if (ducResponse != null) {
                String pSaida = ducResponse.getDucByIdRubricas().getDucByIdRubrica().getpSaida();
                logger.info("Saida: " + pSaida);

                return new CreateDucResponseDto(
                        extractXmlValue(pSaida, "DUC"),
                        extractXmlValue(pSaida, "CODIGO"),
                        extractXmlValue(pSaida, "DESCRICAO"),
                        extractXmlValue(pSaida, "ENTIDADE"),
                        extractXmlValue(pSaida, "REFERENCIA"),
                        extractXmlValue(pSaida, "MONTANTE")
                );
            }
            logger.info("Empty response body");
            throw new RecordNotFoundException("Empty response body");

        }

        logger.info("Error while generating duc");
        throw new CustomInternalServerErrorException("An error occurred while trying to create duc with status code: "+result.getStatusCode());
    }

    @Override
    public List<DucResponseDto> getAllDucs() {
        List<Duc> ducs = ducRepository.findAll();
        if (!ducs.isEmpty()) {
            return ducs.stream().map(duc -> {
                List<DucRubrica> rubricaList = duc.getRubricaList();
                List<RubricaResponseDto> rubricaResponseDtos = new ArrayList<>();
                if (!rubricaList.isEmpty()) {
                    rubricaResponseDtos = rubricaList.stream().map(r -> new RubricaResponseDto(
                            r.getCodRubrica(), r.getValor()
                    )).toList();
                }

                 return new DucResponseDto(duc.getId(), duc.getpRecebedoria(), duc.getpEmail(), String.valueOf(duc.getpNif()),
                        duc.getpMoeda(), duc.getpValor(), duc.getpObs(), duc.getpCodTransacao(), duc.getpCodTransacao1(),
                        duc.getpValor1(), duc.getpCodTransacao2(), duc.getpValor2(), rubricaResponseDtos, duc.getInstituicao(),
                        duc.getDepartamento(), duc.getPlataforma(), duc.getNotas(), duc.getCreatedAt());
            }).toList();
        }
        return List.of();
    }

    private void saveDucInfoInDB(DucRequestDto requestDto) {
        logger.info("Saving DUC request info in DB");
        Duc duc = new Duc(
            requestDto.getpValor(), "CVE", requestDto.getpRecebedoria(), pEmail,
                requestDto.getpNif(), requestDto.getpObs(), requestDto.getpCodTransacao(),
                requestDto.getpCodTransacao1(), requestDto.getpValor1(), requestDto.getpCodTransacao2(),
                requestDto.getpValor2(), requestDto.getInstituicao(), requestDto.getDepartamento(),
                requestDto.getPlataforma(), requestDto.getNotas()
        );
        Duc savedDuc = ducRepository.save(duc);
        if (requestDto.getRubricas() != null && !requestDto.getRubricas().isEmpty()) {

            // TODO get codRuricas from Strapi
            logger.info("RUBRICAS LENGTH "+requestDto.getRubricas().size());
            requestDto.getRubricas().forEach(r -> {
                DucRubrica ducRubrica = new DucRubrica();
                ducRubrica.setDuc(savedDuc);
                ducRubrica.setCodRubrica(r.id().toString());
                ducRubrica.setValor(r.valor());
                ducRubricaRepository.save(ducRubrica);
            });
        }

        logger.info("DUC info saved in DB");
    }

    private String extractXmlValue(String input, String tag) {
        logger.info("extracting xml with input " + input + " and tag " + tag);
        Pattern pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private ResponseEntity<GenerateDucResponse> createDucRequestByArrayIdRubricas(String url, Object body) {
        logger.info("Making request to create duc with body: " + body.toString());

        ResponseEntity<GenerateDucResponse> result =
                webClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + this.token)
                .bodyValue(body)
                .exchangeToMono(response -> response.toEntity(GenerateDucResponse.class))
                .onErrorResume(e -> Mono.error(new RuntimeException(e.getMessage())))
                .block();
        return result;
    }

    private ResponseEntity<DucByTransacaoResponse> createDucRequestByTransacao(String url, DucRequestDto dto) {
        logger.info("Making request to create duc by transacao ");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("p_valor", String.valueOf(dto.getpValor()));
        params.add("p_moeda", moeda);
        params.add("p_recebedoria", dto.getpRecebedoria());
        params.add("p_email", pEmail);
        params.add("p_nif", String.valueOf(dto.getpNif()));
        params.add("p_obs", dto.getpObs());
        params.add("p_codTransacao", dto.getpCodTransacao());
        params.add("p_codTransacao1", dto.getpCodTransacao1());
        params.add("p_valor1", String.valueOf(dto.getpValor1()));
        params.add("p_codTransacao2", dto.getpCodTransacao2());
        params.add("p_valor2", String.valueOf(dto.getpValor2()));
        params.add("accept", "application/json");

        WebClient webClient1 = WebClient.builder().baseUrl(url).build();

        ResponseEntity<DucByTransacaoResponse> result = webClient1.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParams(params)
                        .build())
                .header("Authorization", "Bearer " + this.token)
                .exchangeToMono(response -> response.toEntity(DucByTransacaoResponse.class))
                .onErrorResume(e -> Mono.error(new RuntimeException(e.getMessage())))
                .block();

        return result;

    }


}
