package com.ecommerce.order_service.service;

import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.kafka.OrderProducer;
import com.ecommerce.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;

    public Order placeOrder(Order order) {
        order.setStatus("PLACED");
        Order saved = orderRepository.save(order);

        // Send event to Kafka
        String message = "orderId=" + saved.getId()
                + ",productId=" + saved.getProductId()
                + ",quantity=" + saved.getQuantity()
                + ",email=" + saved.getCustomerEmail();

        orderProducer.sendOrderEvent(message);
        return saved;
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}