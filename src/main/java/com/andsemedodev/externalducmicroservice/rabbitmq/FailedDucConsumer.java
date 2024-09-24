package com.andsemedodev.externalducmicroservice.rabbitmq;

import com.andsemedodev.externalducmicroservice.config.RabbitMqConfig;
import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.service.impl.DucExternalServiceImpl;
import com.andsemedodev.externalducmicroservice.service.impl.DucRequestStatusServiceImpl;
import com.andsemedodev.externalducmicroservice.service.impl.rubrica_request.RubricaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class FailedDucConsumer {

    private final DucExternalServiceImpl ducExternalServiceImpl;
    private Logger logger = LogManager.getLogger(this.getClass());
    private final ObjectMapper objectMapper;
    private final DucExternalServiceImpl ducExternalService;
    private final RabbitTemplate rabbitTemplate;
    private final DucRequestStatusServiceImpl statusService;

    @Value("${duc.by-rubrica-url}")
    private String byRubricaUrl;
    @Value("${duc.by-transacao-url}")
    private String byTransacaoUrl;
    private final long retryDelay = 60000; // 1 minute in milliseconds

    public FailedDucConsumer(ObjectMapper objectMapper, DucExternalServiceImpl ducExternalService, RabbitTemplate rabbitTemplate, DucExternalServiceImpl ducExternalServiceImpl, DucRequestStatusServiceImpl statusService) {
        this.objectMapper = objectMapper;
        this.ducExternalService = ducExternalService;
        this.rabbitTemplate = rabbitTemplate;
        this.ducExternalServiceImpl = ducExternalServiceImpl;
        this.statusService = statusService;
    }
//
//    @RabbitListener(queues = RabbitMqConfig.DUC_REQUESTS)
//    public void proccessFailedDucRequest(DucRequestDto dto) {
//        if (statusService.hasFailed(dto.getRequestId())) {
//            logger.error("Request already failed. Skipping: " + dto.getRequestId());
//            return;
//        }
//        try {
////            FailedDucRequest failedDucRequest = objectMapper.readValue(failedRequestJson, FailedDucRequest.class);
////
////            if (failedDucRequest.getRequestType().equals(RequestType.RUBRICA)) {
////                logger.info("Retrying send duc request by rubrica... " + failedRequestJson);
////                RubricaRequest rubricaRequest = objectMapper.readValue(failedDucRequest.getRequestBody(), RubricaRequest.class);
////                ducExternalService.createDucRequestByArrayIdRubricas(byRubricaUrl, rubricaRequest);
////            } else if (failedDucRequest.getRequestType().equals(RequestType.TRANSACAO)) {
////                logger.info("Retrying send duc request by transacao... " + failedRequestJson);
////                DucRequestDto ducRequestDto = objectMapper.readValue(failedDucRequest.getRequestBody(), DucRequestDto.class);
////                ducExternalService.createDucRequestByTransacao(byTransacaoUrl, ducRequestDto);
////            } else {
////                throw new IllegalArgumentException("Unknown request type "+failedDucRequest.getRequestType());
////            }
//
//            ducExternalServiceImpl.createDucRequestByTransacao(byTransacaoUrl, dto);
//            logger.info("Successfully processed failed DUC request: " + dto);
//
//
//        } catch (Exception e) {
//            logger.error("Error processing failed DUC request: " + e.getMessage());
//
//            // store the failure
//            statusService.markAsFailed(dto.getRequestId());
//
//            rabbitTemplate.convertAndSend(RabbitMqConfig.DUC_EXCHANGE, RabbitMqConfig.RETRY_DUC_REQUESTS, dto);
//            // Let the message go to DLX for retry after TTL
////            throw new AmqpRejectAndDontRequeueException("Failed processing request, message will be retried after TTL", e);
//        }
//    }

    @RabbitListener(queues = "requestQueue")
    public void consumeRequest(String failedRequestJson) {
        try {
            FailedDucRequest failedDucRequest = objectMapper.readValue(failedRequestJson, FailedDucRequest.class);

            if (failedDucRequest.getRequestType().equals(RequestType.RUBRICA)) {
                logger.info("Retrying send duc request by rubrica... " + failedRequestJson);
                RubricaRequest rubricaRequest = objectMapper.readValue(failedDucRequest.getRequestBody(), RubricaRequest.class);
                ducExternalService.createDucRequestByArrayIdRubricas(byRubricaUrl, rubricaRequest);
            } else if (failedDucRequest.getRequestType().equals(RequestType.TRANSACAO)) {
                logger.info("Retrying send duc request by transacao... " + failedRequestJson);
                DucRequestDto ducRequestDto = objectMapper.readValue(failedDucRequest.getRequestBody(), DucRequestDto.class);
                ducExternalService.createDucRequestByTransacao(byTransacaoUrl, ducRequestDto);
            } else {
                throw new IllegalArgumentException("Unknown request type "+failedDucRequest.getRequestType());
            }

            logger.info("Successfully processed failed DUC request");
        } catch (Exception e) {
            // Se falhar novamente, você pode optar por enviar para uma fila de "dead letter" ou tentar reenviar
            logger.error("Request failed again!");
        }
    }
}
