package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.entity.Inventory;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    @PostMapping
    public ResponseEntity<Inventory> addStock(@RequestBody Inventory inventory) {
        return ResponseEntity.ok(inventoryRepository.save(inventory));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getStock(@PathVariable String productId) {
        return ResponseEntity.ok(
                inventoryRepository.findByProductId(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found"))
        );
    }
}