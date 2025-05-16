package com.example.Transaction.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {
    public static final String USER_LOGIN_EXCHANGE = "user.login.exchange";
    public static final String USER_LOGIN_QUEUE = "user.login.queue";
    public static final String USER_LOGIN_ROUTING_KEY = "user.login.routingkey";

    @Bean
    public Queue userLoginQueue() {
        return new Queue(USER_LOGIN_QUEUE);
    }

    @Bean
    public DirectExchange userLoginExchange() {
        return new DirectExchange(USER_LOGIN_EXCHANGE);
    }

    @Bean
    public Binding userLoginBinding(Queue userLoginQueue, DirectExchange userLoginExchange) {
        return BindingBuilder.bind(userLoginQueue)
                .to(userLoginExchange)
                .with(USER_LOGIN_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
