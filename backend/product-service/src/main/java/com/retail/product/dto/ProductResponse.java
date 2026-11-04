package com.retail.product.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String category,
        String sku,
        BigDecimal price,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {}
