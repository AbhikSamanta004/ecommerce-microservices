package com.ecommerce.payment_service.kafka;

import com.ecommerce.payment_service.entity.Payment;
import com.ecommerce.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;

    @KafkaListener(topics = "inventory-events", groupId = "payment-group")
    public void handleInventoryEvent(String message) {
        System.out.println("Payment received: " + message);

        // Parse message
        String[] parts = message.split(",");
        String orderId = parts[0].split("=")[1];
        String productId = parts[1].split("=")[1];
        int quantity = Integer.parseInt(parts[2].split("=")[1]);
        String email = parts[3].split("=")[1];
        String status = parts[4].split("=")[1];

        // Only process if inventory confirmed
        if (!status.equals("CONFIRMED")) {
            System.out.println("Inventory not confirmed. Skipping payment.");
            return;
        }

        // Simulate payment processing
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setProductId(productId);
        payment.setQuantity(quantity);
        payment.setCustomerEmail(email);
        payment.setAmount(quantity * 999.0); // simulate price
        payment.setStatus("SUCCESS");

        paymentRepository.save(payment);

        // Publish to notification service
        String event = "orderId=" + orderId
                + ",productId=" + productId
                + ",quantity=" + quantity
                + ",email=" + email
                + ",amount=" + payment.getAmount()
                + ",status=PAYMENT_SUCCESS";

        paymentProducer.sendPaymentEvent(event);
    }
}