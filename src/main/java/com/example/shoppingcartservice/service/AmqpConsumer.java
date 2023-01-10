package com.example.shoppingcartservice.service;

import com.example.shoppingcartservice.exception.NoItemsFoundException;
import com.example.shoppingcartservice.model.ShoppingCartItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class AmqpConsumer {

    private final ShoppingCartItemService shoppingCartItemService;

    Logger logger = LoggerFactory.getLogger(AmqpConsumer.class);

    @Autowired
    public AmqpConsumer(ShoppingCartItemService shoppingCartItemService) {
        this.shoppingCartItemService = shoppingCartItemService;
    }

    @RabbitListener(queues = "#{addQueue.name}")
    public void addItemToCart(String id, Message message){
        try {
            int itemId = Integer.parseInt(id);
            String userId = message.getMessageProperties().getMessageId();
            shoppingCartItemService.addShoppingCartItem(itemId, userId);
        } catch (Exception e) {
            logger.warn("Item could not be added to cart", e);
        }
    }

    @RabbitListener(queues = "#{deleteQueue.name}")
    public void deleteItemFromCart(String id, Message message){
        try {
            int itemId = Integer.parseInt(id);
            String userId = message.getMessageProperties().getMessageId();
            shoppingCartItemService.deleteShoppingCartItem(itemId, userId);
        } catch (Exception e) {
            logger.warn("Item could not be deleted from cart", e);
        }
    }

//    @RabbitListener(queues="#{orderItemsQueue.name}")
//    @SendTo("status")
//    public Message<String> sendOrderedItems(Message message) {
//        return
//    }
@RabbitListener(queues = "#{orderItemsQueue.name}")
@SendTo("#{exchange.name}/order.items.queried")
public org.springframework.messaging.Message<ShoppingCartItemDTO> processOrder(Message message) {
    MessageProperties messageProperties = message.getMessageProperties();
    String userId = messageProperties.getMessageId();
    String correlationId = messageProperties.getCorrelationId();

    try {
        ShoppingCartItemDTO itemsFromUser = this.shoppingCartItemService.fetchItemsOfUser(userId);

        return MessageBuilder
                .withPayload(itemsFromUser)
                .setHeader("userId", userId)
                .setHeader("correlationId", correlationId)
                .build();
    } catch(Exception e) {
        logger.warn("Shopping Cart Content for message with correlation id {} could not be fetched", correlationId, e);
        throw new NoItemsFoundException();
    }
}
}
