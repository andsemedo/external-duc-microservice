//package com.andsemedodev.externalducmicroservice.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMqConfig {
//    public static final String FAILED_DUC_QUEUE = "failed_duc_queue";
//    public static final String FAILED_DUC_EXCHANGE = "failed-duc-exchange";
//    public static final String FAILED_DUC_ROUTING_KEY = "failed-duc";
//
//    public static final String FAILED_DUC_WAIT_QUEUE = "failed-duc-wait-queue";
//    public static final String FAILED_DUC_WAIT_EXCHANGE = "failed-duc-wait-exchange";
//    public static final String FAILED_DUC_WAIT_ROUTING_KEY = "failed-duc-wait";
//
//    @Bean
//    public Queue failedDucQueue() {
//        return QueueBuilder.durable(FAILED_DUC_QUEUE)
//                .deadLetterExchange(FAILED_DUC_WAIT_EXCHANGE)
//                .deadLetterRoutingKey(FAILED_DUC_WAIT_ROUTING_KEY)
//                .build();
//    }
//
//    @Bean
//    public Queue failedDucWaitQueue() {
//        return QueueBuilder.durable(FAILED_DUC_WAIT_QUEUE)
//                .deadLetterExchange(FAILED_DUC_EXCHANGE)
//                .deadLetterRoutingKey(FAILED_DUC_ROUTING_KEY)
//                .build();
//    }
//
//    @Bean
//    public DirectExchange failedDucExchange() {
//        return new DirectExchange(FAILED_DUC_EXCHANGE);
//    }
//    @Bean
//    DirectExchange failedDucWaitExchange() {
//        return new DirectExchange(FAILED_DUC_WAIT_EXCHANGE);
//    }
//
//    @Bean
//    Binding failedDucBinding() {
//        return BindingBuilder.bind(failedDucQueue())
//                .to(failedDucExchange())
//                .with(FAILED_DUC_ROUTING_KEY);
//    }
//
//    @Bean
//    Binding failedDucWaitBinding() {
//        return BindingBuilder.bind(failedDucWaitQueue())
//                .to(failedDucWaitExchange())
//                .with(FAILED_DUC_WAIT_ROUTING_KEY);
//    }
//
//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setDefaultRequeueRejected(false);
//        factory.setReceiveTimeout(30000L); // 30 seconds shutdown timeout
//        return factory;
//    }
//}
