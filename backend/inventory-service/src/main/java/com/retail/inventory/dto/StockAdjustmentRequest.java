package com.retail.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;

public record StockAdjustmentRequest(@NotNull Integer quantityDelta) {
    @AssertTrue(message = "quantityDelta must not be zero")
    public boolean isNonZero() {
        return quantityDelta == null || quantityDelta != 0;
    }
}
