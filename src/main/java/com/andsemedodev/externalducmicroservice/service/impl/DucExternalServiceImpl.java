package com.andsemedodev.externalducmicroservice.service.impl;

import com.andsemedodev.externalducmicroservice.dto.CreateDucResponseDto;
import com.andsemedodev.externalducmicroservice.exceptions.CustomInternalServerErrorException;
import com.andsemedodev.externalducmicroservice.exceptions.RecordNotFoundException;
import com.andsemedodev.externalducmicroservice.model.Duc;
import com.andsemedodev.externalducmicroservice.repository.DucRepository;
import com.andsemedodev.externalducmicroservice.service.impl.responses.GenerateDucResponse;
import com.andsemedodev.externalducmicroservice.dto.GetDucByNumberResponseDto;
import com.andsemedodev.externalducmicroservice.service.impl.rubrica_request.ProcessBancaArrayId;
import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.dto.Rubricas;
import com.andsemedodev.externalducmicroservice.exceptions.EmptyRubricasException;
import com.andsemedodev.externalducmicroservice.service.DucExternalService;
import com.andsemedodev.externalducmicroservice.service.impl.rubrica_request.RubricaRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DucExternalServiceImpl implements DucExternalService {
    private final Logger logger = Logger.getLogger(DucExternalServiceImpl.class.getName());

    private final WebClient webClient = WebClient.builder().build();
    private String token = "160891a0-c761-377a-b922-a39a420929b4";
    @Value("${duc.pEmail}")
    private String pEmail;

    private final DucRepository ducRepository;

    public DucExternalServiceImpl(DucRepository ducRepository) {
        this.ducRepository = ducRepository;
    }

    @Override
    public CreateDucResponseDto createDuc(DucRequestDto requestDto) {
        logger.info("Creating DUC");

        String url = "https://gateway-pdex.gov.cv/t/financas.gov/rubricaidduc/1.0.0/processBancaArrayId";

        // TODO - search for rubricas in Strapi CMS by its ID and return cod rubrica

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

        // save duc info request in the DB
        logger.info("Saving DUC request info in DB");
        Duc duc = new Duc();
        duc.setInstituicao(requestDto.getInstituicao());
        duc.setDepartamento(requestDto.getDepartamento());
        duc.setPlataforma(requestDto.getPlataforma());
        duc.setNotas(requestDto.getNotas());
        ducRepository.save(duc);
        logger.info("DUC info saved in DB");

        ProcessBancaArrayId processBancaArrayId = new ProcessBancaArrayId(
                requestDto.getpValor(), "CVE", 223, pEmail,
                requestDto.getpNif(), requestDto.getpObs(), idRubricas.toString(), vlRubricas.toString()
        );

        ResponseEntity<GenerateDucResponse> result = createDucRequest(url, new RubricaRequest(processBancaArrayId));

        if (result.getStatusCode().is2xxSuccessful()) {
            logger.info("Request successful to pedex");
            GenerateDucResponse ducResponse = result.getBody();
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

        logger.info("Error while generating duc");
        throw new CustomInternalServerErrorException("An error occurred while trying to create duc with status code: "+result.getStatusCode());
    }

    @Override
    public GetDucByNumberResponseDto getDucPdfByNumber(String ducNumber) throws IOException {
        String url = "http://reportsta.gov.cv/reports/rwservlet?SIGOF_DUC&p_nu_duc=";

        ResponseEntity<byte[]> response = consultarDucRequest(url, ducNumber);
        if (response.getStatusCode().is2xxSuccessful()) {
            byte[] ducPdfBytes = response.getBody();
            logger.info("response from consulting duc with body: "+ Arrays.toString(ducPdfBytes));
            if (ducPdfBytes != null) {
                // C:\var\files\duc\
                Path ducDirectory = Paths.get("/var/files/duc");
                if (!Files.exists(ducDirectory)) {
                    logger.info("Creating duc directory to save pdfs");
                    Files.createDirectories(ducDirectory);
                }

                String pdfFileNameDifer = ducNumber + "-" + Instant.now().getEpochSecond() + ".pdf";
                File pdfFile = new File(ducDirectory.toFile(), "duc-"+pdfFileNameDifer+".pdf");
                try(FileOutputStream fos = new FileOutputStream(pdfFile)) {
                    logger.info("Copying pdf to "+pdfFile.getAbsolutePath());
                    FileCopyUtils.copy(ducPdfBytes, fos);
                    return new GetDucByNumberResponseDto(pdfFile.getAbsolutePath());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save PDF "+e.getMessage());
                }
            }
            throw new RecordNotFoundException("DUC with number "+ducNumber+" not found");
        }
        logger.info("Error while consulting duc: "+ Arrays.toString(response.getBody()));
        throw new CustomInternalServerErrorException("An error occurred while trying to consult duc with status code: "+response.getStatusCode());
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

    private ResponseEntity<GenerateDucResponse> createDucRequest(String url, Object body) {
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

    private ResponseEntity<byte[]> consultarDucRequest(String url, String ducNumber) {
        logger.info("Making request to consult duc ");

        ResponseEntity<byte[]> response = webClient.get()
                .uri(url+ducNumber)
                .retrieve()
                .toEntity(byte[].class)
                .onErrorResume(e -> Mono.error(new RuntimeException(e.getMessage())))
                .block();

        return response;

    }


}
