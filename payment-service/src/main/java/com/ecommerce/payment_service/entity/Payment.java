package com.ecommerce.payment_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private String productId;
    private Integer quantity;
    private String customerEmail;
    private String status; // SUCCESS, FAILED
    private Double amount;
}