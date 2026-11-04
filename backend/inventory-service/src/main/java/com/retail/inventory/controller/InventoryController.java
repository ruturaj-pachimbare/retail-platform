package com.retail.inventory.controller;

import com.retail.inventory.dto.*;
import com.retail.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse create(@Valid @RequestBody CreateInventoryRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<InventoryResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{productId}")
    public InventoryResponse getByProductId(@PathVariable Long productId) {
        return service.getByProductId(productId);
    }

    @PatchMapping("/{productId}/stock")
    public InventoryResponse adjustStock(@PathVariable Long productId,
                                         @Valid @RequestBody StockAdjustmentRequest request) {
        return service.adjustStock(productId, request);
    }

    @PostMapping("/{productId}/reservations")
    public InventoryResponse reserveStock(@PathVariable Long productId,
                                          @Valid @RequestBody StockQuantityRequest request) {
        return service.reserveStock(productId, request);
    }

    @PostMapping("/{productId}/reservations/release")
    public InventoryResponse releaseStock(@PathVariable Long productId,
                                          @Valid @RequestBody StockQuantityRequest request) {
        return service.releaseStock(productId, request);
    }

    @GetMapping("/{productId}/availability")
    public AvailabilityResponse checkAvailability(@PathVariable Long productId,
                                                    @RequestParam(defaultValue = "1") int quantity) {
        return service.checkAvailability(productId, quantity);
    }
}
