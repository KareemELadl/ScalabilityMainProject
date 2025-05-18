package com.example.Transaction.event;

import com.example.Transaction.config.RabbitMQConfig;
import com.example.Transaction.Service.StockConsumedEvent;
import com.example.Transaction.Model.TransactionProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StockEventPublisher {

    private final RabbitTemplate rabbit;

    public void publish(List<TransactionProduct> items) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        System.out.println("Publishing stock consumed event");

                        items.forEach(item -> {
                            Long   orderId    = item.getTransaction().getId();
                            Long   productId  = item.getProduct().getId();
                            Long   merchantId = item.getProduct().getMerchantId();
                            Integer quantity  = item.getQuantity();
                            LocalDateTime createdAt = LocalDateTime.now();

                            StockConsumedEvent ev = new StockConsumedEvent(
                                    orderId,
                                    productId,
                                    merchantId,
                                    quantity,
                                    createdAt
                            );

                            rabbit.convertAndSend(
                                    RabbitMQConfig.ORDER_EXCHANGE,
                                    "order.items.created",
                                    ev
                            );
                        });
                    }
                }
        );
    }

}