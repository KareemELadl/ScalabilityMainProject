package com.example.Transaction.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
public class RabbitMQConfig {

    /* ───────── Exchange ───────── */
    public static final String EXCHANGE = "product.exchange";

    /* ───────── Queues ───────── */
    public static final String PRODUCT_CREATE_QUEUE   = "product.create.queue";
    public static final String PRODUCT_UPDATE_QUEUE   = "product.update.queue";
    public static final String PRODUCT_DELETE_QUEUE   = "product.delete.queue";

    /* ───────── Routing keys ───────── */
    public static final String ROUTING_KEY_CREATE        = "product.created";
    public static final String ROUTING_KEY_UPDATE        = "product.updated";
    public static final String ROUTING_KEY_DELETE        = "product.deleted";
    public static final String ORDER_EXCHANGE = "order.exchange";

    /* ─────────Beans ───────── */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean public Queue createQueue()        { return new Queue(PRODUCT_CREATE_QUEUE, true); }
    @Bean public Queue updateQueue()        { return new Queue(PRODUCT_UPDATE_QUEUE, true); }
    @Bean public Queue deleteQueue()        { return new Queue(PRODUCT_DELETE_QUEUE, true); }


    @Bean
    public Binding createBinding() {
        return BindingBuilder.bind(createQueue()).to(exchange()).with(ROUTING_KEY_CREATE);
    }
    @Bean
    public Binding updateBinding() {
        return BindingBuilder.bind(updateQueue()).to(exchange()).with(ROUTING_KEY_UPDATE);
    }
    @Bean
    public Binding deleteBinding() {
        return BindingBuilder.bind(deleteQueue()).to(exchange()).with(ROUTING_KEY_DELETE);
    }
    // === JSON Message Converter ===
    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(mapper);
    }

    // === RabbitTemplate for Publishing Messages ==
    // =
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

}
