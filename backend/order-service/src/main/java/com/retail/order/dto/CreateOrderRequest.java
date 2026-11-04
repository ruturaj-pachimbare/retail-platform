package com.retail.order.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record CreateOrderRequest(@NotNull @Positive Long customerId,@NotNull @Positive Long productId,
 @NotNull @Positive Integer quantity,@NotNull @DecimalMin("0.01") @Digits(integer=17,fraction=2) BigDecimal unitPrice) {}
