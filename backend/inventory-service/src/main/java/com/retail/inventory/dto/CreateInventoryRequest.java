package com.retail.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateInventoryRequest(
        @NotNull @Positive Long productId,
        @NotBlank @Size(max = 50) String sku,
        @NotNull @PositiveOrZero Integer initialQuantity
) {}
