package com.retail.inventory.dto;

import java.time.Instant;

public record InventoryResponse(
        Long id,
        Long productId,
        String sku,
        int quantityOnHand,
        int quantityReserved,
        int availableQuantity,
        Long version,
        Instant createdAt,
        Instant updatedAt
) {}
