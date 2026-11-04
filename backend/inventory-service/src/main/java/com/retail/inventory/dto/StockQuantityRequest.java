package com.retail.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockQuantityRequest(@NotNull @Positive Integer quantity) {}
