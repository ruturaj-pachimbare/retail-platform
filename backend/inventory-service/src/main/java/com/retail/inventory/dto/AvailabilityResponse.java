package com.retail.inventory.dto;

public record AvailabilityResponse(Long productId, String sku, int requestedQuantity,
                                   int availableQuantity, boolean available) {}
