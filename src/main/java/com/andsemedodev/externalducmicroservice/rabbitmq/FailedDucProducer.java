package com.andsemedodev.externalducmicroservice.rabbitmq;

import com.andsemedodev.externalducmicroservice.dto.DucRequestDto;
import com.andsemedodev.externalducmicroservice.service.impl.DucExternalServiceImpl;
import com.andsemedodev.externalducmicroservice.service.impl.responses.DucByTransacaoResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class FailedDucProducer {

    private final RabbitTemplate rabbitTemplate;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public FailedDucProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendRequestMessage(String ducRequest) {
            rabbitTemplate.convertAndSend("retryQueue", ducRequest);
    }

}
