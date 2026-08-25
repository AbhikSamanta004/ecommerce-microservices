package com.ecommerce.inventory_service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendInventoryEvent(String message) {
        kafkaTemplate.send("inventory-events", message);
        System.out.println("Inventory event sent: " + message);
    }
}