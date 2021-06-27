package com.onlineBroker.InvestmentManagementService.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    //exchange and queue for receiving Order
    public static final String INVESTMENTSERVICE_ORDER_QUEUE = "investmentManagementService_order_queue";
    public static final String ORDERSERVICE_ORDER_EXCHANGE = "orderManagementService_order_exchange";

    //exchange and queue for receiving User-Id
    public static final String INVESTMENTSERVICE_USER_QUEUE = "investmentManagementService_user_queue";
    public static final String USERSERVICE_USER_EXCHANGE = "userManagementService_user_exchange";
    public static final String ROUTING_KEY = "routingKey";

    @Bean
    public Queue investmentservice_order_queue() {
        return new Queue(INVESTMENTSERVICE_ORDER_QUEUE);
    }

    @Bean
    public TopicExchange orderservice_order_exchange() {
        return new TopicExchange(ORDERSERVICE_ORDER_EXCHANGE);
    }

    @Bean
    public Queue investmentservice_user_queue() {
        return new Queue(INVESTMENTSERVICE_USER_QUEUE);
    }

    @Bean
    public TopicExchange userservice_user_exchange() {
        return new TopicExchange(USERSERVICE_USER_EXCHANGE);
    }


    @Bean
    public Binding binding1(@Qualifier("investmentservice_order_queue") Queue queue, @Qualifier("orderservice_order_exchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding binding2(@Qualifier("investmentservice_user_queue") Queue queue, @Qualifier("userservice_user_exchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
