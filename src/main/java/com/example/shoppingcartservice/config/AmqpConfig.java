package com.example.shoppingcartservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    static final String topicExchangeName = "choreography-exchange";

    static final String deletionQueueName = "deletion-queue";

    static final String orderItemsQueue = "order-items-queue";

    static final String addQueueName = "add-queue";

    @Bean
    Queue addQueue() {
        return new Queue(addQueueName, true);
    }

    @Bean
    Queue deleteQueue() {
        return new Queue(deletionQueueName, true);
    }

    @Bean
    Queue orderItemsQueue() {
        return new Queue(orderItemsQueue, true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding addCartItemBinding(Queue addQueue, TopicExchange exchange) {
        return BindingBuilder.bind(addQueue).to(exchange).with("#.add");
    }

    @Bean
    Binding orderItemsBinding(Queue orderItemsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderItemsQueue).to(exchange).with("order-received");
    }


}
