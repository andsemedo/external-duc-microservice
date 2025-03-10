package com.andsemedodev.externalducmicroservice.service.impl;

import com.andsemedodev.externalducmicroservice.dto.RubricasDto;
import com.andsemedodev.externalducmicroservice.exceptions.RecordNotFoundException;
import com.andsemedodev.externalducmicroservice.service.StrapiService;
import com.andsemedodev.externalducmicroservice.service.impl.responses.strapi.StrapiRecebedoria;
import com.andsemedodev.externalducmicroservice.service.impl.responses.strapi.StrapiRecebedoriaResponse;
import com.andsemedodev.externalducmicroservice.service.impl.responses.strapi.StrapiRubrica;
import com.andsemedodev.externalducmicroservice.utilities.StrapiQueries;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class StrapiServiceImpl implements StrapiService {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final StrapiQueries strapiQueries;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${strapi.graphql.url}")
    private String strapiUrl;
    @Value("${strapi.graphql.token}")
    private String strapiToken;
    private final long CACHE_TTL = 3600;

    private final WebClient.Builder webClient = WebClient.builder();

    public StrapiServiceImpl(StrapiQueries strapiQueries, ObjectMapper objectMapper, RedisTemplate<String, Object> redisTemplate) {
        this.strapiQueries = strapiQueries;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<String> fetchRubricasInRecebedoria(String codRecebedoria, List<RubricasDto> rubricasDtoList) {
        logger.info("Fetching rubricas in recebedoria");
        logger.info("Recebedoria code sent: {} ", codRecebedoria);
        logger.info("Rubricas sent: {} ", rubricasDtoList);

        // generate cache key
        String cacheKey = generateCacheKey(codRecebedoria, rubricasDtoList);

        // verify if the value is in cache
        List<String> cachedRubricas = (List<String>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedRubricas != null) {
            logger.info("Returning rubricas in recebedoria from cache");
            return cachedRubricas;
        }

        List<String> listRubricas = rubricasDtoList.stream().map(rub -> rub.id().toString()).toList();
        String query = strapiQueries.getRubricasRecebedoria(codRecebedoria, listRubricas);

        ResponseEntity<String> strapiResponse = this.executeQuery(query);
        List<String> existingRubricas = new ArrayList<>();
        if (strapiResponse.getStatusCode().is2xxSuccessful()) {
            String body = strapiResponse.getBody();
            try {
                StrapiRecebedoriaResponse strapiRecebedoriaResponse = objectMapper.readValue(body, StrapiRecebedoriaResponse.class);
                ArrayList<StrapiRecebedoria> recebedorias = strapiRecebedoriaResponse.getData().getRecebedorias();

                if (recebedorias.isEmpty())
                    throw new RecordNotFoundException("Codigo de recebedoria não encontrado no strapi");

                ArrayList<StrapiRubrica> rubricasInRecebedoria = recebedorias.getFirst().getRubricas();
                rubricasInRecebedoria.forEach(r -> existingRubricas.add(r.getRubrica_id()));

                List<String> rubricasNaoExistentes = rubricasDtoList.stream()
                        .map(RubricasDto::id)
                        .filter(id -> !existingRubricas.contains(id.toString()))
                        .map(String::valueOf)
                        .toList();

                // if any rubrica doesn't exists, throw an exception
                if (!rubricasNaoExistentes.isEmpty()) {
                    throw new RecordNotFoundException("As seguintes rubricas não existem no strapi: " + rubricasNaoExistentes);
                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        logger.info("Rubricas that was found: {} ", existingRubricas);
        redisTemplate.opsForValue().set(cacheKey, existingRubricas, CACHE_TTL, TimeUnit.SECONDS);
        return existingRubricas;
    }

    private String generateCacheKey(String codigoRecebedoria, List<RubricasDto> rubricasDtoList) {
        String rubricaIds = rubricasDtoList.stream()
                .map(r -> r.id().toString())
                .collect(Collectors.joining(","));
        return "recebedoria:" + codigoRecebedoria + ":rubricas:" + rubricaIds;
    }

    private ResponseEntity<String> executeQuery(String document){
        logger.info("Executing query to strapi");

        ResponseEntity<String> result = this.webClient.build()
                .post()
                .uri(strapiUrl)
                .header("Authorization", "Bearer " + strapiToken)
                .bodyValue(Map.of("query", document))
                .exchangeToMono(response -> response.toEntity(String.class))
                .block();

        return result;
    }

}
