package com.ecommerce.payment_service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendPaymentEvent(String message) {
        kafkaTemplate.send("payment-events", message);
        System.out.println("Payment event sent: " + message);
    }
}