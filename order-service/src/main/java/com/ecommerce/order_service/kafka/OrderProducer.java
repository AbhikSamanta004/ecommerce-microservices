package com.ecommerce.order_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendOrderEvent(String message) {
        kafkaTemplate.send("order-events", message);
        System.out.println("Order event sent: " + message);
    }
}