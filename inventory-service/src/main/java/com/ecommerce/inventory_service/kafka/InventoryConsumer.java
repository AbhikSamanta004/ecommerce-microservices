package com.ecommerce.inventory_service.kafka;

import com.ecommerce.inventory_service.entity.Inventory;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryConsumer {

    private final InventoryRepository inventoryRepository;
    private final InventoryProducer inventoryProducer;

    @KafkaListener(topics = "order-events", groupId = "inventory-group")
    public void handleOrderEvent(String message) {
        System.out.println("Inventory received: " + message);

        // Parse message
        String[] parts = message.split(",");
        String orderId = parts[0].split("=")[1];
        String productId = parts[1].split("=")[1];
        int quantity = Integer.parseInt(parts[2].split("=")[1]);
        String email = parts[3].split("=")[1];

        // Check stock
        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElse(null);

        if (inventory != null && inventory.getQuantity() >= quantity) {
            // Reduce stock
            inventory.setQuantity(inventory.getQuantity() - quantity);
            inventoryRepository.save(inventory);

            // Publish to next service
            String event = "orderId=" + orderId
                    + ",productId=" + productId
                    + ",quantity=" + quantity
                    + ",email=" + email
                    + ",status=CONFIRMED";
            inventoryProducer.sendInventoryEvent(event);

        } else {
            // Out of stock
            String event = "orderId=" + orderId
                    + ",email=" + email
                    + ",status=OUT_OF_STOCK";
            inventoryProducer.sendInventoryEvent(event);
            System.out.println("Out of stock for: " + productId);
        }
    }
}