package com.andsemedodev.externalducmicroservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMqConfig {
    public static final String RETRY_DUC_REQUESTS = "retry.duc.requests";
    public static final String DUC_REQUESTS = "duc.requests";
    public static final String DUC_EXCHANGE = "duc-exchange";

    @Bean
    public Queue requestQueue() {
        return new Queue("requestQueue", true);
    }

    @Bean
    public Queue retryQueue() {
        return QueueBuilder.durable("retryQueue")
                .withArgument("x-message-ttl", 30000)  // 1 minuto
                .withArgument("x-dead-letter-exchange", "requestExchange")
                .withArgument("x-dead-letter-routing-key", "requestRoutingKey")
                .build();
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("requestExchange");
    }

    @Bean
    public Binding binding(Queue requestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(requestQueue).to(exchange).with("requestRoutingKey");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
