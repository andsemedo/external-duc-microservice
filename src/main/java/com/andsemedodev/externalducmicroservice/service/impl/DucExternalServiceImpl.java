package com.andsemedodev.externalducmicroservice.service.impl;

import com.andsemedodev.externalducmicroservice.dto.CreateDucResponseDto;
import com.andsemedodev.externalducmicroservice.service.impl.responses.GenerateDucResponse;
import com.andsemedodev.externalducmicroservice.service.impl.rubrica_request.ProcessBancaArrayId;
import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.dto.Rubricas;
import com.andsemedodev.externalducmicroservice.exceptions.EmptyRubricasException;
import com.andsemedodev.externalducmicroservice.service.DucExternalService;
import com.andsemedodev.externalducmicroservice.service.impl.rubrica_request.RubricaRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DucExternalServiceImpl implements DucExternalService {
    private final WebClient webClient = WebClient.builder().build();
    private String token = "160891a0-c761-377a-b922-a39a420929b4";

    @Override
    public CreateDucResponseDto createDuc(DucRequestDto requestDto) {
        String url = "https://gateway-pdex.gov.cv/t/financas.gov/rubricaidduc/1.0.0/processBancaArrayId";


        // TODO - search for rubricas in DB by its ID and return cod rubrica

        // map p_id_rubricas
        List<Rubricas> rubricas = requestDto.getRubricas();
        if (rubricas == null || rubricas.isEmpty())
            throw new EmptyRubricasException("Rubricas nao pode ser nulo ou vazio");
        StringBuilder idRubricas = new StringBuilder();
        StringBuilder vlRubricas = new StringBuilder();

        rubricas.forEach(r -> {
            idRubricas.append(r.id()).append(";");
            vlRubricas.append(r.valor()).append(";");
        });

        ProcessBancaArrayId processBancaArrayId = new ProcessBancaArrayId(
                requestDto.getpValor(), "CVE", 223, "odair.fortes@mf.gov.cv",
                requestDto.getpNif(), requestDto.getpObs(), idRubricas.toString(), vlRubricas.toString()
        );

        ResponseEntity<GenerateDucResponse> result = pedexRequest(url, new RubricaRequest(processBancaArrayId));

        if (result.getStatusCode().is2xxSuccessful()) {
            GenerateDucResponse ducResponse = result.getBody();
            String pSaida = ducResponse.getDucByIdRubricas().getDucByIdRubrica().getpSaida();

            return new CreateDucResponseDto(
                    extractXmlValue(pSaida, "DUC"),
                    extractXmlValue(pSaida, "CODIGO"),
                    extractXmlValue(pSaida, "DESCRICAO"),
                    extractXmlValue(pSaida, "ENTIDADE"),
                    extractXmlValue(pSaida, "REFERENCIA"),
                    extractXmlValue(pSaida, "MONTANTE")
            );

        }

        return null;
    }


    private String extractXmlValue(String input, String tag) {
        Pattern pattern = Pattern.compile("<" + tag + ">(.*?)</" + tag + ">");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private ResponseEntity<GenerateDucResponse> pedexRequest(String url, Object body) {
        ResponseEntity<GenerateDucResponse> result =
                webClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + this.token)
                .bodyValue(body)
                .exchangeToMono(response -> response.toEntity(GenerateDucResponse.class))
                .onErrorResume(e -> Mono.empty())
                .block();
        return result;
    }


}
