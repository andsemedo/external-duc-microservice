//package com.andsemedodev.externalducmicroservice.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMqConfigDump2 {
//
//    public static final String FAILED_DUC_QUEUE = "failed_duc_queue";
//    public static final String RETRY_DUC_QUEUE = "retry_duc_queue";
//    public static final String DLX_DUC_EXCHANGE = "dlx_duc_exchange";
//    private final long retryDelay = 60000;
//    @Bean
//    public Queue failedDucQueue() {
//        return QueueBuilder.durable(FAILED_DUC_QUEUE)
//                .withArgument("x-dead-letter-exchange", DLX_DUC_EXCHANGE) // DLX for failed queue
//                .build();
//    }
//
//    @Bean
//    public Queue retryDucQueue() {
//        return QueueBuilder.durable(RETRY_DUC_QUEUE)
//                .withArgument("x-dead-letter-exchange", "") // Retry to the original queue after TTL
//                .withArgument("x-dead-letter-routing-key", FAILED_DUC_QUEUE)
//                .withArgument("x-message-ttl", retryDelay) // TTL of 1 minute (in milliseconds)
//                .build();
//    }
//
//    @Bean
//    public DirectExchange dlxDucExchange() {
//        return new DirectExchange(DLX_DUC_EXCHANGE);
//    }
//
//    @Bean
//    public Binding dlxBinding() {
//        return BindingBuilder.bind(retryDucQueue())
//                .to(dlxDucExchange())
//                .with(RETRY_DUC_QUEUE);
//    }
//}
