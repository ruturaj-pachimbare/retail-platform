package com.retail.order.dto;
import com.retail.order.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
public record OrderResponse(Long id,Long customerId,Long productId,Integer quantity,BigDecimal unitPrice,
 BigDecimal totalAmount,OrderStatus status,Instant createdAt,Instant updatedAt) {}
